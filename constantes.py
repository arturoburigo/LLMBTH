import json
import random
from typing import List, Dict

def load_functions_data(json_path: str) -> Dict:
    """Carrega o JSON com as informações das funções."""
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    return data['funcoes_formulas']

def generate_question_templates() -> List[str]:
    """Gera templates de perguntas para criar variações."""
    return [
        "Como usar a função {function_name}?",
        "Qual é o propósito da função {function_name}?",
        "Me explique como funciona {function_name}",
        "Preciso {purpose}. Qual função devo usar?",
        "Existe alguma função para {purpose}?",
        "Como faço para {purpose}?",
        "Quais são os parâmetros da função {function_name}?",
        "O que a função {function_name} retorna?",
        "Pode me dar um exemplo de uso da função {function_name}?",
        "Como implementar {purpose} usando as funções disponíveis?"
    ]

def format_return_info(retorno: Dict) -> str:
    """Formata informações de retorno da função de forma segura."""
    info = f"Retorno ({retorno.get('tipo', 'não especificado')}): "
    
    if isinstance(retorno.get('descricao'), str):
        info += retorno['descricao']
    elif 'propriedades' in retorno:
        info += "\nPropriedades:\n"
        for prop, desc in retorno['propriedades'].items():
            info += f"- {prop}: {desc}\n"
    
    return info

def generate_answer_template(function_data: Dict) -> str:
    """Gera um template de resposta baseado nos dados da função."""
    answer = f"A função {function_data['nome']} {function_data['descricao'].lower()}.\n\n"
    
    if 'parametros' in function_data:
        answer += "Parâmetros necessários:\n"
        for param in function_data['parametros']:
            obrigatorio = "obrigatório" if param['obrigatorio'] else "opcional"
            answer += f"- {param['nome']} ({param['tipo']}): {param['descricao']} ({obrigatorio})\n"
    
    if 'retorno' in function_data:
        answer += "\n" + format_return_info(function_data['retorno'])
    
    if 'exemplo' in function_data:
        answer += f"\nExemplo de uso:\n```\n{function_data['exemplo']['codigo']}\n```\n"
        answer += f"Explicação: {function_data['exemplo']['explicacao']}"
    
    return answer

def generate_training_pairs(functions_data: Dict, output_path: str, num_samples: int = 1000):
    """Gera pares de treino no formato do ChatGPT e salva em JSONL."""
    templates = generate_question_templates()
    training_pairs = []
    
    # Para cada categoria de funções
    for category, functions in functions_data.items():
        # Para cada função na categoria
        for func_name, func_data in functions.items():
            # Gera diferentes variações de perguntas
            for template in templates:
                # Substitui placeholders
                question = template.format(
                    function_name=func_name,
                    purpose=func_data['descricao'].lower()
                )
                
                # Gera a resposta
                answer = generate_answer_template(func_data)
                
                # Cria o formato de mensagens do ChatGPT
                training_pair = {
                    "messages": [
                        {"role": "system", "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e APIs do sistema."},
                        {"role": "user", "content": question},
                        {"role": "assistant", "content": answer}
                    ]
                }
                
                training_pairs.append(training_pair)
    
    # Embaralha e limita ao número desejado de amostras
    random.shuffle(training_pairs)
    training_pairs = training_pairs[:num_samples]
    
    # Salva em formato JSONL
    with open(output_path, 'w', encoding='utf-8') as f:
        for pair in training_pairs:
            json.dump(pair, f, ensure_ascii=False)
            f.write('\n')

def main():
    # Configurações
    input_json = "json/funcoes_formulas.json"  # Seu arquivo JSON de entrada
    output_file = "training/training_data_funcoes_formula.jsonl"   # Arquivo de saída
    num_samples = 5000                    # Número de amostras desejadas
    
    # Carrega dados
    functions_data = load_functions_data(input_json)
    
    # Gera pares de treino
    generate_training_pairs(functions_data, output_file, num_samples)
    
    print(f"Gerados {num_samples} pares de treino em {output_file}")

if __name__ == "__main__":
    main()