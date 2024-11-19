// O código verifica se o funcionário possui **Previdência Própria**. Se o evento já foi lançado em variável (`vvar`), ele usa esse valor como o valor calculado. Caso contrário, ele obtém o valor e a referência da **Previdência Própria nas férias** e utiliza esses dados como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado.
Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_PROPRIA)) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        def prevPropriaFerias = Funcoes.getPrevPropriaFerias()
        valorReferencia = prevPropriaFerias.referencia
        valorCalculado = prevPropriaFerias.valor
    }
    if (valorCalculado > 0) {
        evento.replicado(true)
    }
}
