with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "r") as f:
    content = f.read()

# MagritteSkin
content = content.replace(
    "fun MagritteSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {",
    "fun MagritteSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {\n    ScaledLayout(onCycleSkin = onCycleSkin) {"
)
content = content.replace(
    "// -------------------------------------------------------------------------------------\n// SKIN 6: DALI",
    "    }\n}\n// -------------------------------------------------------------------------------------\n// SKIN 6: DALI"
)
content = content.replace(
    ".background(Brush.verticalGradient(listOf(Color(0xFF5E9EC9), Color(0xFF8FBEDC), Color(0xFFCADFE9))))\n            .clickable { onCycleSkin() }",
    ".background(Brush.verticalGradient(listOf(Color(0xFF5E9EC9), Color(0xFF8FBEDC), Color(0xFFCADFE9))))"
)


# DaliSkin
content = content.replace(
    "fun DaliSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {",
    "fun DaliSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {\n    ScaledLayout(onCycleSkin = onCycleSkin) {"
)
content = content.replace(
    "// -------------------------------------------------------------------------------------\n// SKIN 7: CHIRICO",
    "    }\n}\n// -------------------------------------------------------------------------------------\n// SKIN 7: CHIRICO"
)
content = content.replace(
    ".background(Brush.verticalGradient(listOf(Color(0xFFF6DCA8), Color(0xFFF0B57E), Color(0xFFE08A5C))))\n            .clickable { onCycleSkin() }",
    ".background(Brush.verticalGradient(listOf(Color(0xFFF6DCA8), Color(0xFFF0B57E), Color(0xFFE08A5C))))"
)


# ChiricoSkin
content = content.replace(
    "fun ChiricoSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {",
    "fun ChiricoSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {\n    ScaledLayout(onCycleSkin = onCycleSkin) {"
)
content = content.replace(
    "// -------------------------------------------------------------------------------------\n// SKIN 8: JELLYFISH",
    "    }\n}\n// -------------------------------------------------------------------------------------\n// SKIN 8: JELLYFISH"
)
content = content.replace(
    ".background(Brush.verticalGradient(listOf(Color(0xFF0F3A38), Color(0xFF2E6A52), Color(0xFFA8B468))))\n            .clickable { onCycleSkin() }",
    ".background(Brush.verticalGradient(listOf(Color(0xFF0F3A38), Color(0xFF2E6A52), Color(0xFFA8B468))))"
)


# JellyfishSkin
content = content.replace(
    "fun JellyfishSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {",
    "fun JellyfishSkin(\n    arrivals: TrainArrivals,\n    currentTimeSeconds: Long,\n    onCycleSkin: () -> Unit\n) {\n    ScaledLayout(onCycleSkin = onCycleSkin) {"
)
content = content.replace(
    "        Text(\"M Y R T L E – W I L L O U G H B Y   A V S   ·   I N D   C R O S S T O W N\", modifier = Modifier.align(Alignment.TopCenter).offset(y = 24.dp), color = Color(0x66081418), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)\n    }\n}",
    "        Text(\"M Y R T L E – W I L L O U G H B Y   A V S   ·   I N D   C R O S S T O W N\", modifier = Modifier.align(Alignment.TopCenter).offset(y = 24.dp), color = Color(0x66081418), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)\n    }\n    }\n}"
)
content = content.replace(
    ".background(Color(0xFF0C191A))\n            .clickable { onCycleSkin() }",
    ".background(Color(0xFF0C191A))"
)


# Fix the double closing brackets I just added (since the skin already had the closing `}` but the replacement prepended `}\n}` to the next comment)
# Wait, replacing `// SKIN 6: DALI` with `}\n}\n// SKIN 6: DALI` means I'm adding `}\n}` right *before* the comment. But the original code was `    }\n}\n\n// SKIN 6: DALI`.
# So I should replace `    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 6: DALI`
content = content.replace("    }\n}\n    }\n}\n//", "    }\n}\n}\n//") # Cleanup if there's an extra bracket
content = content.replace(
    "        Text(\"La persistance du départ.\", modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = (-30).dp), color = Color(0x991E3A4C), fontSize = 34.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 6: DALI",
    "        Text(\"La persistance du départ.\", modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = (-30).dp), color = Color(0x991E3A4C), fontSize = 34.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)\n    }\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 6: DALI"
)
content = content.replace(
    "        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 90.dp, y = 134.dp).height(184.dp).clipToBounds()) {\n            Text(sbTrains.getOrNull(0) ?: \"--\", fontSize = 240.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFFBEED3), modifier = Modifier.skewX(-3f).graphicsLayer(scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false), shadow = Shadow(Color(0x595A281E), Offset(0f, 6f), 18f)))\n        }\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 7: CHIRICO",
    "        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 90.dp, y = 134.dp).height(184.dp).clipToBounds()) {\n            Text(sbTrains.getOrNull(0) ?: \"--\", fontSize = 240.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFFBEED3), modifier = Modifier.skewX(-3f).graphicsLayer(scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false), shadow = Shadow(Color(0x595A281E), Offset(0f, 6f), 18f)))\n        }\n    }\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 7: CHIRICO"
)
content = content.replace(
    "        Text(\"L’enigma dell’arrivo.\", modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = (-26).dp), color = Color(0xFFF6E8C8), fontSize = 32.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 8: JELLYFISH",
    "        Text(\"L’enigma dell’arrivo.\", modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = (-26).dp), color = Color(0xFFF6E8C8), fontSize = 32.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)\n    }\n    }\n}\n\n// -------------------------------------------------------------------------------------\n// SKIN 8: JELLYFISH"
)

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "w") as f:
    f.write(content)
