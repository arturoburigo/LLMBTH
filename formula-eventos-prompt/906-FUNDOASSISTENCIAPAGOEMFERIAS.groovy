// O código verifica se o funcionário possui Assistência Municipal. Se o evento já foi lançado em variável (vvar), ele usa esse valor como o valor calculado. Caso contrário, ele obtém o valor e a referência da Assistência Municipal nas férias e os utiliza como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado. 
Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.ASSISTENCIA_MUNICIPAL)) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        def assistMunicipalFerias = Funcoes.getAssistMunicipalFerias()
        valorReferencia = assistMunicipalFerias.referencia
        valorCalculado = assistMunicipalFerias.valor
    }
    if (valorCalculado > 0) {
        evento.replicado(true)
    }
}
