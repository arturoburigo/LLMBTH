def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.PRORROGACAO_DA_LICENCA_MATERNIDADE,
        ClassificacaoTipoAfastamento.PRORROGACAO_DA_LICENCA_MATERNIDADE_11_770
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
  if ( existeAfastamento ) continue;
  existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
