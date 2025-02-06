import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToeGame() {
    var isDarkMode by remember { mutableStateOf(false) }
    val gridSize = 3
    val cellSizeDp = 200.dp / gridSize
    val cellSizePx = with(LocalDensity.current) { cellSizeDp.toPx() }
    var grid by remember { mutableStateOf(Array(gridSize) { CharArray(gridSize) { ' ' } }) }
    var currentPlayer by remember { mutableStateOf('X') }
    var winner by remember { mutableStateOf<Char?>(null) }

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color.White
    val gridColor = if (isDarkMode) Color.LightGray else Color.Gray
    val textColor = if (isDarkMode) Color.White else Color.Yellow
    val xColor = if (isDarkMode) Color.Cyan else Color(0xFFE91E63)
    val oColor = if (isDarkMode) Color.Yellow else Color.Blue

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor)
    ) {
        TopAppBar(
            modifier =Modifier.fillMaxWidth()
                .background(color = Color.Green),
            title = { Text(text = "Fares*(X & O)*", color = textColor) },
            actions = {
                IconButton(onClick = { isDarkMode = !isDarkMode }) {
                    Icon(imageVector = Icons.Default.Brightness4, contentDescription = "Toggle Theme", tint = textColor)
                }
                IconButton(onClick = {
                    grid = Array(gridSize) { CharArray(gridSize) { ' ' } }
                    winner = null
                    currentPlayer = 'X'
                }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart Game", tint = textColor)
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier
                .size(200.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val x = (offset.x / cellSizePx).toInt()
                        val y = (offset.y / cellSizePx).toInt()
                        if (grid[y][x] == ' ' && winner == null) {
                            grid = grid.copyOf().also { it[y][x] = currentPlayer }
                            winner = checkWinner(grid)
                            if (winner == null) {
                                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                            }
                        }
                    }
                }) {
                for (i in 1 until gridSize) {
                    drawLine(
                        color = gridColor,
                        start = Offset(x = cellSizePx * i, y = 0f),
                        end = Offset(x = cellSizePx * i, y = 200.dp.toPx()),
                        strokeWidth = 4.dp.toPx()
                    )
                    drawLine(
                        color = gridColor,
                        start = Offset(x = 0f, y = cellSizePx * i),
                        end = Offset(x = 200.dp.toPx(), y = cellSizePx * i),
                        strokeWidth = 4.dp.toPx()
                    )
                }
                for (i in 0 until gridSize) {
                    for (j in 0 until gridSize) {
                        val offsetX = cellSizePx * j
                        val offsetY = cellSizePx * i
                        when (grid[i][j]) {
                            'X' -> drawX(offsetX, offsetY, cellSizePx, xColor)
                            'O' -> drawO(offsetX, offsetY, cellSizePx, oColor)
                        }
                    }
                }
            }
            if (winner != null) {
                Text(
                    text = "Winner: ${winner ?: "No One"}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun checkWinner(grid: Array<CharArray>): Char? {
    for (i in 0 until 3) {
        if (grid[i][0] != ' ' && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) return grid[i][0]
        if (grid[0][i] != ' ' && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]) return grid[0][i]
    }
    if (grid[0][0] != ' ' && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) return grid[0][0]
    if (grid[0][2] != ' ' && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) return grid[0][2]
    return null
}

fun DrawScope.drawX(x: Float, y: Float, size: Float, color: Color) {
    drawLine(color, Offset(x, y), Offset(x + size, y + size), strokeWidth = 5.dp.toPx())
    drawLine(color, Offset(x + size, y), Offset(x, y + size), strokeWidth = 5.dp.toPx())
}

fun DrawScope.drawO(x: Float, y: Float, size: Float, color: Color) {
    drawCircle(color, radius = size / 2, center = Offset(x + size / 2, y + size / 2))
}
