if ( matricula.isCompensacaoHoras() ){
  if (apuracao.dataApuracaoFormatado == apuracao.dataFinal){
    Date dataInicial = apuracao.dataInicial
    Date dataFinal = Datas.removeDias(apuracao.dataFinal, 1)
    def extras = Ocorrencias.valor(3, dataInicial, dataFinal) + Ocorrencias.valor(3);
    extras += Ocorrencias.valor(4, dataInicial, dataFinal) + Ocorrencias.valor(4);
    def faltas = Ocorrencias.valor(5, dataInicial, dataFinal) + Ocorrencias.valor(5);
    faltas += Ocorrencias.valor(6, dataInicial, dataFinal) + Ocorrencias.valor(6);
    BigDecimal saldo = 0;
    if ( faltas > extras ){
      saldo = faltas - extras;
      compensacao.inserirCompensacaoHoras(apuracao.dataApuracaoFormatado, saldo, TipoCompensacaoHoras.COMPENSACAO_HORAS_EXTRAS)
    }
    valorApurado = saldo;
  }
}
