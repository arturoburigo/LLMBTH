//O código calcula o valor de abatimento relacionado à Assistência Municipal para funcionários que possuem essa modalidade de previdência. Se houver um valor lançado manualmente no evento, ele será utilizado. Caso contrário, verifica o valor integral associado ao evento de férias (código 90). Se esse valor for positivo, utiliza o valor do evento 906 como o abatimento. Por fim, se o valor do abatimento calculado for maior que zero, ele é atribuído como resultado final e o evento é marcado como replicado.

Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.ASSISTENCIA_MUNICIPAL)) {
    double valorAbatimento
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorAbatimento = vvar
    } else {
        // Verifica se o funcionário tem valor de fundo de assistência pago em férias integrais no evento 90
        def valorFundoAssistFeriasIntegral = Funcoes.getValorCodigoEventoFerias(90, true).valor
        // Se o valor for maior que zero, utiliza o valor do evento 906 como abatimento
        if (valorFundoAssistFeriasIntegral > 0) {
            valorAbatimento = Eventos.valor(906)
        }
    }
    // Se o valor do abatimento calculado em variavel for maior que zero, utiliza esse valor e marca o evento como replicado
    if (valorAbatimento > 0) {
        valorCalculado = valorAbatimento
        evento.replicado(true)
    }
}
