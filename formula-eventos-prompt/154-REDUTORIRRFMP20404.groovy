if (Datas.data(calculo.competencia.ano, calculo.competencia.mes, 1) < Datas.data(2004, 8, 1) ||
    Datas.data(calculo.competencia.ano, calculo.competencia.mes, 1) > Datas.data(2004, 12, 1)) {
    suspender "Este cálculo é executado apenas entre o período de 08/2004 à 12/2004"
}
Funcoes.somenteFuncionarios()
if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
    suspender "O evento não deve ser calculado em processamentos rescisórios"
}
if (Bases.valor(Bases.PAGAPROP) <= 0) {
    suspender "Não é possível calcular funcionários sem valor de base 'Paga proporcional' na competência"
}
valorCalculado = 100
Bases.compor(valorCalculado, Bases.IRRF, Bases.IRRF13)