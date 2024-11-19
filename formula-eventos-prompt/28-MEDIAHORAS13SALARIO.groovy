Funcoes.somenteFuncionarios()
if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
    suspender 'Este evento não é calculado no processamento de rescisão. Para este processamento deve haver um evento específico por conta das rubricas do eSocial'
}
if (!Funcoes.permitecalc13integral()) {
    suspender 'A matrícula não tem direito a receber décimo terceiro ou o seu período aquisitivo contém uma situação não permitida para o cálculo do evento'
}
int avos13
int avos13Fgts
double valBasePropCalc
double valBaseFgts
def vvar = Lancamentos.valor(evento)
if (vvar >= 0) {
    valBasePropCalc = vvar
    valorCalculado = valBasePropCalc
    valBaseFgts = vvar
} else {
    if (!(TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento) && SubTipoProcessamento.INTEGRAL.equals(calculo.subTipoProcessamento))) {
        suspender 'Este evento deve ser calculado apenas no processamento de décimo terceiro (integral)'
    }
    int avosAuxMat13 = Funcoes.avosAuxMat13()
    avos13Fgts = Funcoes.avos13(12, true) - avosAuxMat13
    avos13 = Funcoes.avos13(12) - avosAuxMat13
    if (avos13 <= 0) {
        suspender 'Não há avos adquiridos no período aquisitivo de décimo terceiro'
    }
    double base = Eventos.valor(29) + Eventos.valor(30) + Eventos.valor(233) //apenas para gerar dependência
    valBasePropCalc = mediaVantagem.calcular(avos13)
    valorCalculado = valBasePropCalc
    valBaseFgts = (valBasePropCalc * 12 / avos13) * avos13Fgts / 12
}
valorReferencia = avos13
Bases.compor(valorCalculado,
        Bases.IRRF13,
        Bases.INSS13,
        Bases.PREVEST13,
        Bases.FUNDASS13,
        Bases.FUNDPREV13,
        Bases.FUNDFIN13)
Bases.compor(valBaseFgts, Bases.FGTS13)
double valEsocialFgts = valBaseFgts - valBasePropCalc
Bases.compor(valEsocialFgts, Bases.FGTS13AFASES)
