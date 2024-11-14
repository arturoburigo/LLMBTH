import json
import random
from typing import List, Dict, Any

def load_variables_data(json_path: str) -> Dict:
    """Carrega o JSON com as informações das variáveis reservadas."""
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    return data['variaveis_reservadas']

def generate_variable_templates() -> List[str]:
    """Gera templates de perguntas para variáveis."""
    return [
        "Como usar a variável {variable_name}?",
        "O que é a variável {variable_name}?",
        "Para que serve a variável {variable_name}?",
        "Me explique a variável {variable_name}",
        "Quais informações posso obter da variável {variable_name}?",
        "Como acessar {purpose} usando {variable_name}?",
        "Quais são as propriedades disponíveis em {variable_name}?",
        "Como funciona a variável {variable_name}?",
        "Pode me dar um exemplo de uso da variável {variable_name}?",
        "O que representa a variável {variable_name}?"
    ]

def format_property(prop_name: str, prop_data: Dict) -> str:
    """Formata a descrição de uma propriedade."""
    if isinstance(prop_data, dict):
        result = f"- {prop_name} ({prop_data.get('tipo', 'tipo não especificado')}): {prop_data.get('descricao', '')}"
        if 'exemplo' in prop_data:
            result += f"\n  Exemplo:\n  ```\n  {prop_data['exemplo']}\n  ```"
        return result
    return f"- {prop_name}: {prop_data}"

def format_method(method_name: str, method_data: Dict) -> str:
    """Formata a descrição de um método."""
    result = f"- {method_name}: {method_data.get('descricao', '')}\n"
    
    if 'parametros' in method_data:
        result += "  Parâmetros:\n"
        for param in method_data['parametros']:
            result += f"  * {param['nome']} ({param['tipo']}): {param['descricao']}\n"
    
    if 'retorno' in method_data:
        if isinstance(method_data['retorno'], dict):
            result += f"  Retorno: {method_data['retorno'].get('descricao', '')}\n"
            if 'propriedades' in method_data['retorno']:
                result += "  Propriedades do retorno:\n"
                for prop in method_data['retorno']['propriedades']:
                    if isinstance(prop, dict):
                        result += f"    * {prop['nome']}: {prop['descricao']}\n"
        else:
            result += f"  Retorno: {method_data['retorno']}\n"
    
    if 'exemplo' in method_data:
        result += f"  Exemplo:\n  ```\n  {method_data['exemplo']['codigo']}\n  ```\n"
        result += f"  Explicação: {method_data['exemplo']['explicacao']}\n"
    
    return result

def process_variable(var_data: Dict, var_name: str) -> List[Dict]:
    """Processa uma variável reservada e gera pares de treino."""
    training_pairs = []
    templates = generate_variable_templates()
    
    for template in templates:
        question = template.format(
            variable_name=var_name,
            purpose=var_data.get('descricao', '').lower()
        )
        
        # Gera resposta detalhada
        answer = f"A variável {var_name} {var_data.get('descricao', '').lower()}\n\n"
        
        # Adiciona informações sobre o tipo se disponível
        if 'tipo' in var_data:
            answer += f"Tipo: {var_data['tipo']}\n\n"
        
        # Adiciona propriedades se existirem
        if 'propriedades' in var_data:
            answer += "Propriedades disponíveis:\n"
            for prop_name, prop_data in var_data['propriedades'].items():
                answer += format_property(prop_name, prop_data) + "\n"
        
        # Adiciona métodos se existirem
        if 'metodos' in var_data:
            answer += "\nMétodos disponíveis:\n"
            for method_name, method_data in var_data['metodos'].items():
                answer += format_method(method_name, method_data) + "\n"
        
        # Adiciona exemplo se existir
        if 'exemplo' in var_data:
            answer += f"\nExemplo de uso:\n```\n{var_data['exemplo']['codigo']}\n```\n"
            answer += f"Explicação: {var_data['exemplo']['explicacao']}\n"
        
        # Adiciona observações se existirem
        if 'observacoes' in var_data:
            answer += "\nObservações importantes:\n"
            for obs in var_data['observacoes']:
                answer += f"- {obs}\n"
        
        training_pairs.append({
            "messages": [
                {
                    "role": "system",
                    "content": "Você é um assistente especializado em auxiliar desenvolvedores com as variáveis reservadas do sistema de folha de pagamento."
                },
                {
                    "role": "user",
                    "content": question
                },
                {
                    "role": "assistant",
                    "content": answer.strip()
                }
            ]
        })
    
    return training_pairs

def generate_training_pairs(variables_data: Dict, output_path: str, num_samples: int = 1000):
    """Gera pares de treino para todas as variáveis reservadas."""
    all_training_pairs = []
    
    # Processa cada variável reservada
    for var_name, var_data in variables_data.items():
        training_pairs = process_variable(var_data, var_name)
        all_training_pairs.extend(training_pairs)
        
        # Gera perguntas adicionais sobre propriedades específicas
        if 'propriedades' in var_data:
            for prop_name, prop_data in var_data['propriedades'].items():
                question = f"Como acessar a propriedade {prop_name} da variável {var_name}?"
                answer = format_property(prop_name, prop_data)
                
                all_training_pairs.append({
                    "messages": [
                        {
                            "role": "system",
                            "content": "Você é um assistente especializado em auxiliar desenvolvedores com as variáveis reservadas do sistema de folha de pagamento."
                        },
                        {
                            "role": "user",
                            "content": question
                        },
                        {
                            "role": "assistant",
                            "content": f"Para acessar a propriedade {prop_name} da variável {var_name}:\n\n{answer}"
                        }
                    ]
                })
    
    # Adiciona exemplos de uso combinado de variáveis
    combined_examples = [
        {
            "question": "Como calcular o salário proporcional usando as variáveis reservadas?",
            "answer": "Para calcular o salário proporcional, você pode usar as variáveis 'funcionario' e 'valorCalculado' da seguinte forma:\n\n```\nvalorCalculado = Funcoes.calcprop(funcionario.salario, funcionario.quantidadeHorasMes)\n```\n\nIsso vai calcular o valor proporcional do salário baseado nas horas mensais do funcionário."
        },
        {
            "question": "Como verificar dados do servidor e seus dependentes?",
            "answer": "Você pode combinar a variável 'servidor' com seus métodos e propriedades assim:\n\n```\nint idade = Funcoes.idade(servidor.dataNascimento, Datas.hoje())\nint dependentesIRRF = servidor.dependentesIrrf\nList dependentes = servidor.buscaDependentes()\n```\n\nIsso permite acessar a idade do servidor, quantidade de dependentes para IRRF e lista completa de dependentes."
        }
    ]
    
    for example in combined_examples:
        all_training_pairs.append({
            "messages": [
                {
                    "role": "system",
                    "content": "Você é um assistente especializado em auxiliar desenvolvedores com as variáveis reservadas do sistema de folha de pagamento."
                },
                {
                    "role": "user",
                    "content": example["question"]
                },
                {
                    "role": "assistant",
                    "content": example["answer"]
                }
            ]
        })
    
    # Embaralha e limita ao número desejado de amostras
    random.shuffle(all_training_pairs)
    all_training_pairs = all_training_pairs[:num_samples]
    
    # Salva em formato JSONL
    with open(output_path, 'w', encoding='utf-8') as f:
        for pair in all_training_pairs:
            json.dump(pair, f, ensure_ascii=False)
            f.write('\n')
    
    return len(all_training_pairs)

def main():
    # Configurações
    input_json = "json/variaveis_reservadas.json"
    output_file = "training/training_data_variaveis_reservadas.jsonl"
    num_samples = 5000
    
    # Carrega dados
    variables_data = load_variables_data(input_json)
    
    # Gera pares de treino
    total_pairs = generate_training_pairs(variables_data, output_file, num_samples)
    
    print(f"Gerados {total_pairs} pares de treino em {output_file}")

if __name__ == "__main__":
    main()