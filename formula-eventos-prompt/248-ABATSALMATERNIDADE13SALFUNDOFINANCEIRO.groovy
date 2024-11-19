Funcoes.somenteFuncionarios()
if (!TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento) && !(TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento) && SubTipoProcessamento.INTEGRAL.equals(calculo.subTipoProcessamento))) {
    suspender "Este evento deve ser calculado apenas nos processamentos de rescisão e décimo terceiro (integral)"
}
if (!Funcoes.permiteCalculoAuxilioMaternidade()) {
    suspender "A matrícula não tem direito a receber o abatimento salário maternidade"
}
if (!Funcoes.possuiPrevidencia(TipoPrevidencia.FUNDO_FINANCEIRO)) {
    suspender "Este cálculo é realizado apenas para matrículas contribuintes do fundo financeiro"
}
if (!Funcoes.permitecalc13integral()) {
    suspender 'A matrícula não tem direito a receber décimo terceiro ou o seu período aquisitivo contém uma situação não permitida para o cálculo do evento'
}
double base = Bases.valor(Bases.FUNDFIN13)
if (base <= 0) {
    suspender "Não há valor de base de fundo financeiro décimo terceiro salário para cálculo"
}
double vlrCalc
def vvar = Lancamentos.valor(evento)
if (vvar >= 0) {
    vlrCalc = vvar
} else {
    vlrCalc = Funcoes.deducauxmat13(base, Funcoes.avos13(12), true)
}
valorCalculado = vlrCalc - Funcoes.buscaValorEvento13SalarioIntegralAdiantado()
