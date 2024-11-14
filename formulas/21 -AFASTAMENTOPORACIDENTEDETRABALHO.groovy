def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.SERVICO_MILITAR,
        ClassificacaoTipoAfastamento.ACIDENTE_DE_TRABALHO_EMPREGADOR,
        ClassificacaoTipoAfastamento.ACIDENTE_DE_TRAJETO_PREVIDENCIA,
        ClassificacaoTipoAfastamento.ACIDENTE_DE_TRAJETO_EMPREGADOR
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
  if ( existeAfastamento ) continue;
  existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
