import json
import random
from typing import List, Dict, Any

def load_functions_data(json_path: str) -> Dict:
    """Carrega o JSON com as informações das funções fórmulas."""
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    return data['funcoes_formulas']

def generate_function_templates() -> List[str]:
    """Gera templates de perguntas para funções."""
    return [
        "Como usar a função {function_name}?",
        "O que faz a função {function_name}?",
        "Me explique como funciona a função {function_name}",
        "Preciso {purpose}. Como faço isso?",
        "Quais são os parâmetros da função {function_name}?",
        "O que a função {function_name} retorna?",
        "Pode me dar um exemplo de uso da função {function_name}?",
        "Como implementar {purpose}?",
        "Qual função devo usar para {purpose}?",
        "Existe uma função para {purpose}?"
    ]

def generate_category_templates() -> List[str]:
    """Gera templates de perguntas para categorias de funções."""
    return [
        "Quais funções existem para {category}?",
        "Me explique as funções de {category}",
        "Como funcionam as funções de {category}?",
        "Quais são as funções disponíveis para {category}?",
        "O que posso fazer com as funções de {category}?"
    ]

def format_parameters(parameters: List[Dict]) -> str:
    """Formata a lista de parâmetros de uma função."""
    if not parameters:
        return "Esta função não possui parâmetros."
    
    result = "Parâmetros:\n"
    for param in parameters:
        obrigatorio = "obrigatório" if param.get('obrigatorio', False) else "opcional"
        result += f"- {param['nome']} ({param['tipo']}): {param['descricao']} ({obrigatorio})\n"
    return result

def format_return_info(retorno: Dict) -> str:
    """Formata as informações de retorno da função."""
    result = f"\nRetorno ({retorno.get('tipo', 'não especificado')}): {retorno.get('descricao', '')}"
    
    if 'propriedades' in retorno:
        result += "\nPropriedades do retorno:\n"
        for prop, desc in retorno['propriedades'].items():
            result += f"- {prop}: {desc}\n"
    
    return result

def process_function(func_data: Dict, func_name: str, category_name: str) -> List[Dict]:
    """Processa uma única função e gera pares de treino."""
    training_pairs = []
    templates = generate_function_templates()
    
    for template in templates:
        question = template.format(
            function_name=func_name,
            purpose=func_data.get('descricao', '').lower()
        )
        
        # Gera resposta detalhada
        answer = f"A função {func_name} {func_data.get('descricao', '').lower()}. "
        answer += f"Esta função pertence ao grupo de {category_name}.\n\n"
        
        if 'parametros' in func_data:
            answer += format_parameters(func_data['parametros'])
        
        if 'retorno' in func_data:
            answer += format_return_info(func_data['retorno'])
        
        if 'exemplo' in func_data:
            answer += f"\nExemplo de uso:\n```\n{func_data['exemplo']['codigo']}\n```\n"
            answer += f"Explicação: {func_data['exemplo']['explicacao']}"
        
        training_pairs.append({
            "messages": [
                {
                    "role": "system",
                    "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e fórmulas do sistema de folha de pagamento."
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

def process_category(category_data: Dict, category_name: str) -> List[Dict]:
    """Processa uma categoria de funções e gera pares de treino."""
    training_pairs = []
    category_templates = generate_category_templates()
    
    # Gera pares para a categoria
    for template in category_templates:
        question = template.format(category=category_name.replace('funcoes_', ''))
        
        # Lista todas as funções da categoria
        answer = f"As seguintes funções estão disponíveis para {category_name.replace('funcoes_', '')}:\n\n"
        for func_name, func_data in category_data.items():
            answer += f"- {func_name}: {func_data.get('descricao', '')}\n"
        
        training_pairs.append({
            "messages": [
                {
                    "role": "system",
                    "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e fórmulas do sistema de folha de pagamento."
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
    
    # Processa cada função da categoria
    for func_name, func_data in category_data.items():
        training_pairs.extend(process_function(func_data, func_name, category_name))
    
    return training_pairs

def generate_training_pairs(functions_data: Dict, output_path: str, num_samples: int = 1000):
    """Gera pares de treino para todas as funções fórmulas."""
    all_training_pairs = []
    
    # Processa cada categoria de funções
    for category_name, category_data in functions_data.items():
        training_pairs = process_category(category_data, category_name)
        all_training_pairs.extend(training_pairs)
    
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
    input_json = "json/funcoes_formulas.json"
    output_file = "training/training_data_funcoes_formulas.jsonl"
    num_samples = 5000
    
    # Carrega dados
    functions_data = load_functions_data(input_json)
    
    # Gera pares de treino
    total_pairs = generate_training_pairs(functions_data, output_file, num_samples)
    
    print(f"Gerados {total_pairs} pares de treino em {output_file}")

if __name__ == "__main__":
    main()
    