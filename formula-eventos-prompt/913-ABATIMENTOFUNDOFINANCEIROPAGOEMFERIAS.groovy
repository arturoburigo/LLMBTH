// O código calcula o valor de abatimento relacionado à Previdência do tipo "Fundo Financeiro" para funcionários que possuem essa modalidade. Ele utiliza um valor previamente registrado no evento, se disponível. Caso contrário, verifica o valor integral associado ao código de férias específico (249) e, se positivo, utiliza o valor do evento 908 como o abatimento. Se o valor calculado do abatimento for maior que zero, ele é definido como o resultado final e o evento é marcado como replicado.

// Função que verifica se é funcionario
Funcoes.somenteFuncionarios()
// Verifica se o funcionário possui previdência do tipo "Fundo Financeiro"
if (funcionario.possuiPrevidencia(TipoPrevidencia.FUNDO_FINANCEIRO)) {
    // Variável que armazena o valor do abatimento
    double valorAbatimento
    // Variável que armazena o valor do evento em variavel
    def vvar = Lancamentos.valor(evento)
    // Se o valor do evento for maior que zero, o valor do abatimento é definido como o valor do evento
    if (vvar > 0) {
        valorAbatimento = vvar
    } else {
        // Se o valor do evento for zero, o valor do codigo de evento de ferias 249 é definido como o valor do fundo financeiro de ferias integral
        def valorFundoFinancFeriasIntegral = Funcoes.getValorCodigoEventoFerias(249, true).valor
        // Se o valor do fundo financeiro de ferias integral for maior que zero, o valor do evento 908 é definido como o valor do abatimento
        if (valorFundoFinancFeriasIntegral > 0) {
            valorAbatimento = Eventos.valor(908)
        }
    }
    // Se o valor do abatimento for maior que zero, o valor calculado é definido como o valor do abatimento e o evento é marcado como replicado
    if (valorAbatimento > 0) {
        valorCalculado = valorAbatimento
        evento.replicado(true)
    }
}
