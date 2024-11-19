//Este código calcula o valor líquido das férias de um funcionário com base em condições específicas. Primeiro, verifica se o valor do evento é maior que zero. Se for, utiliza esse valor como o líquido de férias. Caso contrário, calcula o líquido de férias como o total das férias menos o valor do evento 900, somado ao valor do evento 901. Por fim, se o líquido de férias for positivo, ele é atribuído a valorCalculado e o evento é marcado como replicado.

// Variável que armazena o valor líquido das férias
double liquidoFerias
// Variável que armazena o valor do evento lançado em variavel
def vvar = Lancamentos.valor(evento)
// Verifica se o valor do evento é maior que zero
if (vvar > 0) {
    // Se for, o valor do evento é atribuído ao valor líquido das férias
    liquidoFerias = vvar
} else {
    // Se não, o valor líquido das férias é calculado como o total das férias menos o valor do evento 900, somado ao valor do evento 901
    liquidoFerias = Funcoes.getTotalFerias().liquido - Eventos.valor(900) + Eventos.valor(901)
}
// Verifica se o valor líquido das férias é positivo
if (liquidoFerias > 0) {
    // Se for, o valor líquido das férias é atribuído a valorCalculado
    valorCalculado = liquidoFerias
    // O evento é marcado como replicado
    evento.replicado(true)
}
