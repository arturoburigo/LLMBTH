// O código verifica se o funcionário possui Previdência do tipo Fundo Financeiro. Se o evento já foi lançado em variável (vvar), ele usa esse valor como o valor calculado. Caso contrário, ele obtém o valor e a referência do Fundo Financeiro nas férias e os utiliza como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado.

Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.FUNDO_FINANCEIRO)) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        def fundoFinancFerias = Funcoes.getFundoFinanceiroFerias()
        valorReferencia = fundoFinancFerias.referencia
        valorCalculado = fundoFinancFerias.valor
    }
    if (valorCalculado > 0) {
        evento.replicado(true)
    }
}
