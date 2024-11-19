// O código verifica se a matrícula do funcionário é do tipo FUNCIONÁRIO ou ESTAGIÁRIO. Se não for nenhum desses tipos, ele suspende a execução com a mensagem "Este cálculo é executado apenas para funcionários e estagiários". Em seguida, o código verifica se o evento já foi lançado em variável (vvar). Se o valor de vvar for maior que zero, ele é utilizado como o valor calculado. Caso contrário, o código obtém o valor e a referência do IRRF nas férias e utiliza esses dados como o valor calculado. Se o valor calculado for maior que zero, o evento é marcado como replicado.
if (!TipoMatricula.FUNCIONARIO.equals(matricula.tipo) && !TipoMatricula.ESTAGIARIO.equals(matricula.tipo)) {
    suspender "Este cálculo é executado apenas para funcionários e estagiários"
}
def vvar = Lancamentos.valor(evento)
if (vvar > 0) {
    valorCalculado = vvar
} else {
    def irrfFerias = Funcoes.getIrrfFerias()
    valorReferencia = irrfFerias.referencia
    valorCalculado = irrfFerias.valor
}
if (valorCalculado > 0) {
    evento.replicado(true)
}
