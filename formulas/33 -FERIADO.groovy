classificacaoPeriodo = jornada.getClassificacaoPeriodo(apuracao.dataApuracaoFormatado)
if ( feriado.isFeriado() || feriado.isPontoFacultativo() ){
  if ( enquadramento.horas(TipoHoras.TRABALHADAS) <= 0 ){
    valorApurado = horario.cargaHoraria
  }
}
