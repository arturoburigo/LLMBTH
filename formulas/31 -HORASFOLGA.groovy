classificacaoPeriodo = jornada.getClassificacaoPeriodo(apuracao.dataApuracaoFormatado)
if (classificacaoPeriodo == TipoClassificacaoPeriodo.FOLGA){
  if ( enquadramento.horas(TipoHoras.TRABALHADAS) <= 0 ){
    valorApurado = horario.cargaHoraria
  }
}
