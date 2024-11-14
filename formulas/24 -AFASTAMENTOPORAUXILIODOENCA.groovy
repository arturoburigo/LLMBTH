def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.AUXILIO_DOENCA_PREVIDENCIA,
        ClassificacaoTipoAfastamento.DOENCA_DO_TRABALHO_PREVIDENCIA,
        ClassificacaoTipoAfastamento.AUXILIO_DOENCA_EMPREGADOR,
        ClassificacaoTipoAfastamento.DOENCA_DO_TRABALHO_EMPREGADOR
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
  if ( existeAfastamento ) continue;
  existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
