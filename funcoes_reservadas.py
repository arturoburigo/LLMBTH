import json
import random
from typing import List, Dict, Any

def load_functions_data(json_path: str) -> Dict:
    """Carrega o JSON com as informações das funções reservadas."""
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    return data['funcoes_reservadas']

def generate_method_templates() -> List[str]:
    """Gera templates de perguntas para métodos."""
    return [
        "Como usar o método {method_name} de {item_name}?",
        "O que faz o método {method_name} de {item_name}?",
        "Me explique como funciona o método {method_name}",
        "Como implementar {purpose} usando o método {method_name}?",
        "Quais são os parâmetros do método {method_name}?"
    ]

def generate_property_templates() -> List[str]:
    """Gera templates de perguntas para propriedades."""
    return [
        "O que é a propriedade {item_name}?",
        "Para que serve a propriedade {item_name}?",
        "Me explique a propriedade {item_name}",
        "Qual o tipo da propriedade {item_name}?",
        "Como usar a propriedade {item_name}?"
    ]

def generate_class_templates() -> List[str]:
    """Gera templates de perguntas para classes/objetos."""
    return [
        "Como usar {item_name}?",
        "Qual é o propósito de {item_name}?",
        "Me explique como funciona {item_name}",
        "Quais são os recursos disponíveis em {item_name}?",
        "Pode me dar um exemplo de uso de {item_name}?"
    ]

def format_parameters(parameters: List[Dict]) -> str:
    """Formata a lista de parâmetros de um método."""
    if not parameters:
        return "Não possui parâmetros"
    
    result = "Parâmetros:\n"
    for param in parameters:
        obrigatorio = "obrigatório" if param.get('obrigatorio', False) else "opcional"
        result += f"- {param['nome']} ({param['tipo']}): {param['descricao']} ({obrigatorio})\n"
    return result

def format_return_info(retorno: Dict) -> str:
    """Formata as informações de retorno do método."""
    if not retorno:
        return ""
    
    result = f"\nRetorno ({retorno.get('tipo', 'não especificado')}): "
    if 'descricao' in retorno:
        result += retorno['descricao']
    
    if 'propriedades' in retorno:
        result += "\nPropriedades:\n"
        if isinstance(retorno['propriedades'], dict):
            for prop, desc in retorno['propriedades'].items():
                result += f"- {prop}: {desc}\n"
        elif isinstance(retorno['propriedades'], list):
            for prop in retorno['propriedades']:
                if isinstance(prop, dict):
                    result += f"- {prop.get('nome', '')}: {prop.get('descricao', '')}\n"
    return result

def process_methods(methods: Dict, parent_name: str) -> List[Dict]:
    """Processa os métodos de uma função/classe."""
    training_pairs = []
    templates = generate_method_templates()
    
    for method_name, method_data in methods.items():
        for template in templates:
            question = template.format(
                item_name=parent_name,
                method_name=method_name,
                purpose=method_data.get('descricao', '').lower()
            )
            
            answer = f"{method_data.get('descricao', '')}\n\n"
            if 'parametros' in method_data:
                answer += format_parameters(method_data['parametros'])
            if 'retorno' in method_data:
                answer += format_return_info(method_data['retorno'])
            if 'exemplo' in method_data:
                answer += f"\nExemplo:\n```\n{method_data['exemplo']['codigo']}\n```\n"
                answer += f"Explicação: {method_data['exemplo']['explicacao']}"
            
            training_pairs.append({
                "messages": [
                    {
                        "role": "system",
                        "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e APIs do sistema."
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

def process_properties(properties: Dict, parent_name: str) -> List[Dict]:
    """Processa as propriedades de uma função/classe."""
    training_pairs = []
    templates = generate_property_templates()
    
    for prop_name, prop_data in properties.items():
        for template in templates:
            question = template.format(
                item_name=f"{prop_name} de {parent_name}"
            )
            
            answer = f"A propriedade {prop_name} de {parent_name} "
            if isinstance(prop_data, dict):
                answer += f"({prop_data.get('tipo', 'tipo não especificado')}) "
                answer += prop_data.get('descricao', '')
            else:
                answer += str(prop_data)
            
            training_pairs.append({
                "messages": [
                    {
                        "role": "system",
                        "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e APIs do sistema."
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

def process_function(func_data: Dict, func_name: str) -> List[Dict]:
    """Processa uma função reservada e seus subcomponentes."""
    training_pairs = []
    
    # Adiciona perguntas gerais sobre a função/classe
    templates = generate_class_templates()
    for template in templates:
        question = template.format(item_name=func_name)
        answer = f"{func_name} é uma função reservada do sistema. "
        
        if 'descricao' in func_data:
            answer += func_data['descricao']
        
        training_pairs.append({
            "messages": [
                {
                    "role": "system",
                    "content": "Você é um assistente especializado em auxiliar desenvolvedores com as funções e APIs do sistema."
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
    
    # Processa métodos
    if 'metodos' in func_data:
        training_pairs.extend(process_methods(func_data['metodos'], func_name))
    
    # Processa propriedades
    if 'propriedades' in func_data:
        training_pairs.extend(process_properties(func_data['propriedades'], func_name))
    
    # Processa atributos (similar a propriedades)
    if 'atributos' in func_data:
        training_pairs.extend(process_properties(func_data['atributos'], func_name))
    
    # Processa subgrupos recursivamente
    for key, value in func_data.items():
        if key.startswith('subgrupos'):
            for subgroup_name, subgroup_data in value.items():
                training_pairs.extend(process_function(subgroup_data, subgroup_name))
    
    return training_pairs

def generate_training_pairs(functions_data: Dict, output_path: str, num_samples: int = 1000):
    """Gera pares de treino para todas as funções reservadas."""
    all_training_pairs = []
    
    # Processa cada função reservada
    for func_name, func_data in functions_data.items():
        training_pairs = process_function(func_data, func_name)
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
    input_json = "json/funcoes_reservadas.json"
    output_file = "training/training_data_funcoes_reservadas.jsonl"
    num_samples = 5000
    
    # Carrega dados
    functions_data = load_functions_data(input_json)
    
    # Gera pares de treino
    total_pairs = generate_training_pairs(functions_data, output_file, num_samples)
    
    print(f"Gerados {total_pairs} pares de treino em {output_file}")

if __name__ == "__main__":
    main()