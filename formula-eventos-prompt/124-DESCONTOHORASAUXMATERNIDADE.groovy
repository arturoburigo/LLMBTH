Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidenciaFederal) {
    def diasafast = Funcoes.afasadocao()
    if ((servidor.sexo.equals(Sexo.MASCULINO)) || (calculo.competencia > Datas.data(1999, 11, 1) && calculo.competencia <= Datas.data(2003, 8, 1))) {
        diasafast += Funcoes.afaslicmat()
        if (diasafast <= 0) {
            suspender "Não há afastamentos com as classificações 'Licença maternidade - Adoção/guarda judicial de criança' ou 'Licença maternidade - 120 dias' na competência"
        }
    }
    if (diasafast <= 0) {
        suspender "Não há afastamentos com as classificações 'Licença maternidade - Adoção/guarda judicial de criança' na competência"
    }
    def dias
    def vaux = Funcoes.cnvdpbase(diasafast)
    def base = Bases.valor(Bases.INSS) + Eventos.valor(40)
    if (calculo.dataRescisao != null) {
        def dataRescisao = calculo.dataRescisao
        def dataAdmissao = funcionario.dataAdmissao
        dias = Datas.dia(dataRescisao)
        if (Datas.ano(dataAdmissao) == Datas.ano(calculo.competencia) && Datas.mes(dataAdmissao) == Datas.mes(calculo.competencia)) {
            dias -= (Datas.dia(dataAdmissao) + 1)
        }
        base = base * funcionario.quantidadeHorasMes / Funcoes.cnvdpbase(dias)
        base = Numeros.arredonda(base, 2)
        valorCalculado = Funcoes.calcprop(base, vaux)
    } else {
        valorReferencia = vaux
        if (diasafast == 30) {
            valorCalculado = base
        } else {
            def pagp = Bases.valor(Bases.PAGAPROP)
            dias = Funcoes.diastrab() + diasafast
            if (dias < 30) {
                if (UnidadePagamento.HORISTA.equals(funcionario.unidadePagamento)) {
                    base *= funcionario.quantidadeHorasMes
                } else {
                    base *= 30
                }
                base /= pagp
                base = Numeros.arredonda(base, 2)
            }
            valorCalculado = Funcoes.calcprop(base, vaux)
        }
    }
    Bases.compor(valorCalculado, Bases.DEDUCIRRFMES, Bases.EXCEINSS)
}
