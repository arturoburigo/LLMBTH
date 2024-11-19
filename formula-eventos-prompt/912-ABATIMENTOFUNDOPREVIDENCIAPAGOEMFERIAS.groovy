//O código calcula o valor de abatimento relacionado à Previdência Própria para funcionários que possuem essa modalidade. Se houver um valor lançado manualmente no evento, ele é utilizado. Caso contrário, o código verifica o valor integral associado ao evento de férias (código 91). Se esse valor for positivo, utiliza o valor do evento 907 como o abatimento. Por fim, se o valor do abatimento for maior que zero, ele é atribuído como resultado final e o evento é marcado como replicado.

Funcoes.somenteFuncionarios()
// Função que verifica se o funcionário possui Previdência Própria
if (funcionario.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_PROPRIA)) {
    // Variável que armazena o valor do abatimento
    double valorAbatimento
    // Variável que armazena o valor do evento
    def vvar = Lancamentos.valor(evento)
    // Se o valor do evento for maior que zero, ele é atribuído como valor do abatimento
    if (vvar > 0) {
        valorAbatimento = vvar
    } else {
        // Se o valor do evento for zero, é verificado o valor integral do evento de férias 91
        def valorFundoPrevFeriasIntegral = Funcoes.getValorCodigoEventoFerias(91, true).valor
        // Se o valor do evento de férias for maior que zero, o valor do evento 907 é atribuído como valor do abatimento
        if (valorFundoPrevFeriasIntegral > 0) {
            valorAbatimento = Eventos.valor(907)
        }
    }
    // Se o valor do abatimento for maior que zero, ele é atribuído como resultado final e o evento é marcado como replicado
    if (valorAbatimento > 0) {
        valorCalculado = valorAbatimento
        evento.replicado(true)
    }
}
