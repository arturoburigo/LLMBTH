def classificacoesTipoAusencia = [
        ClassificacaoTipoAusencia.CONSULTA_MEDICA,
        ClassificacaoTipoAusencia.EXAME_MEDICO
]
double totalApurado = 0
for (classificacaoTipoAusencia in classificacoesTipoAusencia) {
  totalApurado += ausencia.totalAusencia(classificacaoTipoAusencia)
}
valorApurado = totalApurado
