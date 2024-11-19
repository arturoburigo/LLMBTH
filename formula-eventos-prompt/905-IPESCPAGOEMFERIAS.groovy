//O código verifica se o funcionário possui Previdência Estadual. Se o evento já foi lançado em variável (vvar), ele usa esse valor como o valor calculado. Caso contrário, ele obtém o valor e a referência da Previdência Estadual nas férias e utiliza esses dados como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado.
Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_ESTADUAL)) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        def prevEstadualFerias = Funcoes.getPrevEstadualFerias()
        valorReferencia = prevEstadualFerias.referencia
        valorCalculado = prevEstadualFerias.valor
    }
    if (valorCalculado > 0) {
        evento.replicado(true)
    }
}
