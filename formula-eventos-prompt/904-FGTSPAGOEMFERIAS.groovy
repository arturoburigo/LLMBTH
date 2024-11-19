// O código verifica se o funcionário é optante do FGTS. Se o evento já foi lançado em variável (vvar), ele usa esse valor como o valor calculado. Caso contrário, ele obtém o valor e a referência do FGTS nas férias e utiliza esses dados como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado.
Funcoes.somenteFuncionarios()
if (funcionario.optanteFgts) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        def fgtsFerias = Funcoes.getfgtsFerias()
        valorReferencia = fgtsFerias.referencia
        valorCalculado = fgtsFerias.valor
    }
    if (valorCalculado > 0) {
        evento.replicado(true)
    }
}
