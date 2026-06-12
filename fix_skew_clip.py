import re

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "r") as f:
    content = f.read()

# Add imports if missing
imports = [
    "import androidx.compose.ui.draw.drawWithContent",
    "import androidx.compose.ui.graphics.drawscope.drawIntoCanvas"
]
for imp in imports:
    if imp not in content:
        content = content.replace("import androidx.compose.ui.text.font.Font", imp + "\nimport androidx.compose.ui.text.font.Font")

# Add skewX extension
skew_code = """
// Extension for skew
fun Modifier.skewX(degrees: Float, pivotX: Float = 0f, pivotY: Float = 1f) = this.drawWithContent {
    val rad = Math.toRadians(degrees.toDouble()).toFloat()
    val tan = kotlin.math.tan(rad.toDouble()).toFloat()
    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(size.width * pivotX, size.height * pivotY)
        canvas.skew(tan, 0f)
        canvas.translate(-size.width * pivotX, -size.height * pivotY)
        drawContent()
        canvas.restore()
    }
}
"""
if "fun Modifier.skewX" not in content:
    content = content.replace("@Composable\nfun DaliSkin", skew_code + "\n@Composable\nfun DaliSkin")

# DaliSkin fixes:
# 1. Soft watch skew
content = content.replace(".graphicsLayer(rotationZ = -16f, scaleX = 0.9f)", ".graphicsLayer(rotationZ = -16f).skewX(-8f, 0.5f, 0.5f)")
content = content.replace(".graphicsLayer(rotationZ = 4f)", ".skewX(8f, 0.5f, 0.5f).graphicsLayer(rotationZ = 4f)")

# 2. Shadows skew instead of graphicsLayer scale
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 60f, translationY = 80f)", "modifier = Modifier.offset(x = 60.dp, y = 80.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f)")
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 40f, translationY = 50f)", "modifier = Modifier.offset(x = 40.dp, y = 50.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f)")
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 20f, translationY = 30f)", "modifier = Modifier.offset(x = 20.dp, y = 30.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f)")

# 3. Clip melting 2 and move drips to 318
content = content.replace("Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 90.dp, y = 134.dp)) {", "Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 90.dp, y = 134.dp).height(184.dp).clipToBounds()) {")
content = content.replace("modifier = Modifier.graphicsLayer(rotationZ = -3f, scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f))", "modifier = Modifier.skewX(-3f).graphicsLayer(scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f))")

content = content.replace("Drip(120, 336", "Drip(120, 318")
content = content.replace("Drip(178, 336", "Drip(178, 318")
content = content.replace("Drip(236, 336", "Drip(236, 318")

# ChiricoSkin fixes:
# The shadows need skewX instead of rotation
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 140f, translationY = 80f, rotationZ = -20f)", "modifier = Modifier.offset(x = 140.dp, y = 80.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f)")
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 100f, translationY = 60f, rotationZ = -20f)", "modifier = Modifier.offset(x = 100.dp, y = 60.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f)")
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 70f, translationY = 50f, rotationZ = -20f)", "modifier = Modifier.offset(x = 70.dp, y = 50.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f)")
content = content.replace("modifier = Modifier.graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f), translationX = 50f, translationY = 40f, rotationZ = -20f)", "modifier = Modifier.offset(x = 50.dp, y = 40.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f)")

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "w") as f:
    f.write(content)
