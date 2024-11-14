def diasMes(data){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Datas.ano(data), (Datas.mes(data) -1 )  , 1 );
    return calendar.getActualMaximum(Calendar.DATE);
}
valorApurado = 0;
if (apuracao.dataApuracaoFormatado == apuracao.dataFinal){
    def dataInicial = apuracao.dataInicial;
    def dataFinal = apuracao.dataFinal;
    def mes = Datas.mes(dataFinal);
    def competencia = Datas.data(Datas.ano(apuracao.dataInicialCompetencia), mes, 1)
    def dias, qtddsr = 0, dataApuAux, qtdDias;
    Boolean usaPeriodoApuracao = Boolean.TRUE;     //Usar o período da apuração no PARA,//Usar o período da competência no PARA
    Boolean consideraPonto = Boolean.TRUE; //Considerar ponto facultativo como feriado //Não considerar ponto facultativo como feriado;
    if (usaPeriodoApuracao) {
        qtdDias = Datas.diferencaDias(dataInicial,dataFinal);
        dataApuAux = dataInicial;
    } else {
        qtdDias = diasMes(competencia)-1;
        dataApuAux = competencia;
    }
    for(dias = 0; dias <= qtdDias; dias++) {
        //imprimir "Analisando data..  : ${Datas.formatar(dataApuAux, 'dd/MM/yyyy')}"
        if ((Datas.diaSemana(dataApuAux) == 1) ||
                (feriado.consulta(dataApuAux, false)) ||
                (feriado.consulta(dataApuAux, true) && consideraPonto)) {
            qtddsr++;
        }
        dataApuAux = Datas.adicionaDias(dataApuAux,1);
    }
    valorApurado = qtddsr;
}
