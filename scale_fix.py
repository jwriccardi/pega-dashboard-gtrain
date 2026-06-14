import re

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "r") as f:
    content = f.read()

# 1. Inject ScaledLayout definition
scaled_layout_code = """
@Composable
fun ScaledLayout(onCycleSkin: () -> Unit, content: @Composable () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color.Black).clickable { onCycleSkin() },
        contentAlignment = Alignment.Center
    ) {
        val scale = minOf(maxWidth.value / 1180f, maxHeight.value / 664f)
        Box(
            modifier = Modifier
                .requiredSize(1180.dp, 664.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.5f)
                }
                .clipToBounds()
        ) {
            content()
        }
    }
}
"""

if "fun ScaledLayout" not in content:
    content = content.replace("// SKIN 5: MAGRITTE", scaled_layout_code + "\n// SKIN 5: MAGRITTE")

# 2. For each skin, replace the outer Box with ScaledLayout

def replace_skin(skin_name, content):
    pattern = r"fun " + skin_name + r"\((.*?)\) \{([\s\S]*?)Box \(\n\s*modifier = Modifier\n\s*\.fillMaxSize\(\)\n\s*\.background\((.*?)\)\n\s*\.clickable \{ onCycleSkin\(\) \}\n\s*\) \{([\s\S]*?)\n\s*\}\n\}"
    
    def replacer(match):
        args = match.group(1)
        pre_box = match.group(2)
        bg = match.group(3)
        inner = match.group(4)
        
        return f"""fun {skin_name}({args}) {{{pre_box}ScaledLayout(onCycleSkin = onCycleSkin) {{
        Box(modifier = Modifier.fillMaxSize().background({bg})) {{{inner}
        }}
    }}
}}"""

    return re.sub(pattern, replacer, content)

content = replace_skin("MagritteSkin", content)
content = replace_skin("DaliSkin", content)
content = replace_skin("ChiricoSkin", content)
content = replace_skin("JellyfishSkin", content)

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "w") as f:
    f.write(content)
