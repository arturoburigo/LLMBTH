classificacaoPeriodo = jornada.getClassificacaoPeriodo(apuracao.dataApuracaoFormatado)
if ( classificacaoPeriodo.equals(TipoClassificacaoPeriodo.COMPENSADO) ){
  if ( enquadramento.horas(TipoHoras.TRABALHADAS) <= 0 ){
    valorApurado = horario.cargaHoraria
  }
}
