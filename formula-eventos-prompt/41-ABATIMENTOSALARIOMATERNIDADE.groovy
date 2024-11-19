if (!TipoMatricula.FUNCIONARIO.equals(matricula.tipo) && !TipoMatricula.AUTONOMO.equals(matricula.tipo)) {
    suspender "Este cálculo é executado apenas para funcionários ou autônomos"
}
if (!servidor.sexo.equals(Sexo.FEMININO)) {
    suspender "Este cálculo é realizado apenas para funcionários do sexo feminino"
}
if (!Funcoes.possuiPrevidenciaFederal(matricula.tipo)) {
    suspender "Este cálculo é realizado apenas para matrículas contribuintes da previdência federal"
}
if (TipoMatricula.AUTONOMO.equals(matricula.tipo)) {
    if (calculo.competencia < Datas.data(2003, 4, 1)) {
        suspender "Para autônomos, este cálculo deve ser executado apenas em competências posteriores a 03/2003"
    }
    if (autonomo.codESocial.equals("741")) {
        suspender "Não há desconto de contribuição previdenciária para autônomos da categoria MEI com o 'Código eSocial' igual a '741' informado na categoria do trabalhador"
    }
}
def datainicial = Datas.data(Datas.ano(calculo.competencia), Datas.mes(calculo.competencia), 1)
def datafinal = Datas.data(Datas.ano(calculo.competencia), Datas.mes(calculo.competencia), calculo.quantidadeDiasCompetencia)
def classificacoes = [ClassificacaoTipoAfastamento.LICENCA_MATERNIDADE,
                      ClassificacaoTipoAfastamento.PRORROGACAO_DA_LICENCA_MATERNIDADE,
                      ClassificacaoTipoAfastamento.ABORTO_NAO_CRIMINOSO]
def dias = Funcoes.diasafast(datainicial, datafinal, classificacoes)
if (dias <= 0) {
    suspender "Não há dias de afastamento com as classificações 'Licença maternidade', 'Licença maternidade - Antecipação ou prorrogação' ou 'Licença maternidade - Aborto não criminoso' na competência"
}
def vaux = Lancamentos.valor(evento)
if (vaux >= 0) {
    valorCalculado = vaux
} else {
    def vsmataux = 0
    def dadosMatricula = Funcoes.remuneracao(matricula.tipo)
    def ultdia = datafinal
    def diaaf = Funcoes.diasafast(ultdia, ultdia, classificacoes)
    if (diaaf == 0 && SubTipoProcessamento.COMPLEMENTAR.equals(calculo.subTipoProcessamento)) {
        suspender "Não há dias de afastamento com as classificações 'Licença maternidade', 'Licença maternidade - Antecipação ou prorrogação' ou 'Licença maternidade - Aborto não criminoso' a serem calculados na subtipo de processamento complementar"
    }
    def base = (Bases.valor(Bases.INSS) + Eventos.valor(40)) -
               (Eventos.valor(22) +
                Eventos.valor(23) +
                Eventos.valor(24) +
                Eventos.valor(178) +
                Eventos.valor(179) +
                Eventos.valor(180))
    def diasLicMaternidade = Funcoes.cnvdpbase(Funcoes.afaslicmat()) + Funcoes.cnvdpbase(Funcoes.afasprorroglicmat()) + Funcoes.cnvdpbase(Funcoes.afasaborto())
    def pagaProporcional = Bases.valor(Bases.PAGAPROP)
    if (base == dadosMatricula.valor) {
        vsmataux = dadosMatricula.valor
    } else {
        def baseaux
        def hrsmesaux
        if (UnidadeCalculo.DIAS.equals(calculo.unidade)) {
            baseaux = (dadosMatricula.quantidadeHorasMes / 30) * pagaProporcional
            baseaux = Numeros.arredonda(baseaux, 2)
            hrsmesaux = Numeros.arredonda(dadosMatricula.quantidadeHorasMes, 2)
        } else {
            baseaux = pagaProporcional
            hrsmesaux = dadosMatricula.quantidadeHorasMes
        }
        if (baseaux != hrsmesaux) {
            vsmataux = Eventos.valor(3) + Eventos.valor(161) + Eventos.valor(162)
        }
    }
    if ((vsmataux != dadosMatricula.valor) && (Eventos.valor(195) == 0)) {
        vsmataux = Eventos.valor(3) + Eventos.valor(161) + Eventos.valor(162)
        if (dias < calculo.quantidadeDiasCompetencia) {
            def diafin = pagaProporcional
            def dtrescisao = calculo.dataRescisao
            def diaresc = 0
            if (dtrescisao) {
                diaresc = dtrescisao.dia
            }
            if ((dtrescisao) && (Funcoes.cnvdpbase(diaresc) < diafin)) {
                diafin = Funcoes.cnvdpbase(diaresc)
            }
            if (datainicial >= Datas.data(2003, 8, 1)) {
                if (UnidadePagamento.HORISTA.equals(dadosMatricula.unidade) || UnidadePagamento.DIARISTA.equals(dadosMatricula.unidade)) {
                    diafin = diafin + (dadosMatricula.quantidadeHorasMes / 30)
                }
                base = base - Bases.valor(Bases.COMPHORAMES)
                base = ((base * diasLicMaternidade) / diafin) + vsmataux
            } else {
                if (dtrescisao) {
                    base /= Datas.dia(dtrescisao)
                    base = Numeros.trunca(base, 2) * calculo.quantidadeDiasCompetencia
                }
                base /= calculo.quantidadeDiasCompetencia
                base = Numeros.trunca(base, 2) * dias
            }
        }
        valorCalculado = base
    } else {
        valorCalculado = Eventos.valor(3) + Eventos.valor(161) + Eventos.valor(162) + Eventos.valor(195)
    }
}
