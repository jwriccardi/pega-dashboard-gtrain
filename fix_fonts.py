import re

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "r") as f:
    content = f.read()

# define oswaldFont in each skin
content = content.replace("val timeFormatter = remember", "val oswaldFont = FontFamily(Font(R.font.oswald_bold))\n    val timeFormatter = remember")

# add fontFamily = oswaldFont to Text elements that are big numbers (fontSize >= 50.sp)
# For Magritte:
content = re.sub(r'fontSize = (\d+)\.sp,\s*fontWeight = FontWeight\.(Black|Bold),\s*color = ([^,]+),', r'fontSize = \1.sp, fontWeight = FontWeight.\2, fontFamily = oswaldFont, color = \3,', content)

# For Dali, the text-shadow: skewX... We can fix transformOrigin:
content = content.replace("scaleY = 0.3f,", "scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f),")
content = content.replace("scaleY = 0.4f,", "scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f),")
content = content.replace("scaleY = 1.12f)", "scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f))")

# Add the dots on Dali ledge
dots = """Box(modifier = Modifier.fillMaxWidth().height(22.dp).background(Color(0xFFB27A4E)))
            Row(modifier = Modifier.padding(top = 28.dp, start = 30.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(13) { Box(modifier = Modifier.size(6.dp).background(Color(0xFF2A1B25), CircleShape)) }
            }"""
content = content.replace("Box(modifier = Modifier.fillMaxWidth().height(22.dp).background(Color(0xFFB27A4E)))", dots)

with open("app/src/main/java/ai/pegasusgrowth/gtraindash/ui/SurrealSkins.kt", "w") as f:
    f.write(content)
