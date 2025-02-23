package com.andredigital.catancounter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CatanCell(
    state: CatanCardState = CatanCardState(),
    onDeleteItem: (String) -> Unit,
    onItemChanged: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        shape = CardDefaults.shape
    ) {
        state.rollNumberIcons?.let { icons ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            ) {
                Text(
                    text = state.rollNumber.toString(),
                    modifier = Modifier
                        .padding(end = 8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Image(
                    painter = painterResource(icons.first.iconId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .size(24.dp)
                )

                Image(
                    painter = painterResource(icons.second.iconId),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            state.items.forEach { item ->
                CatanResourceItem(
                    item = item,
                    onDeleteItem = onDeleteItem,
                    onItemChanged = onItemChanged
                )
            }
        }

    }
}

@Composable
private fun CatanResourceItem(
    item: CatanResourceItem,
    onDeleteItem: (String) -> Unit,
    onItemChanged: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(40.dp)
    ) {
        Text(
            text = "x${item.count.collectAsState().value}",
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Text(
            text = item.resourceName.toString(),
            modifier = Modifier
                .padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                item.decrementCount()
                onItemChanged()
            },
            modifier = Modifier
            //.padding(top = 4.dp)

        ) {
            Icon(
                imageVector = Icons.Sharp.Remove,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {
                item.incrementCount()
                onItemChanged()
            },
        ) {
            Icon(
                imageVector = Icons.Sharp.Add,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { onDeleteItem(item.getKey()) },
        ) {
            Icon(
                imageVector = Icons.Sharp.Delete,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatanResourcePicker(
    displayPicker: Boolean,
    onAddResource: (CatanResourceItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (displayPicker) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color(0xFF96939B))
                .padding(8.dp)
        ) {
            val values = remember { (2..12).toList() }
            val valuesPickerState = rememberPickerState()

            Picker(
                state = valuesPickerState,
                items = values,
                visibleItemsCount = 5,
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                textModifier = Modifier.padding(8.dp),
                textStyle = TextStyle(fontSize = 32.sp),
                dividerColor = Color(0xFFE8E8E8)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                val items = CatanResource.toList()
                itemsIndexed(
                    items = items,
                    span = { index, _ ->
                        GridItemSpan(
                            currentLineSpan = if (index == 4) 2 else 1
                        )
                    }
                ) { _, resource ->
                    Box(
                        modifier = Modifier
                            .widthIn(max = 200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                val item = CatanResourceItem(
                                    rollNumber = valuesPickerState.selectedItem,
                                    resourceName = resource
                                )
                                onAddResource(item)
                                onDismiss()
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2DC7FF)
                            )
                        ) {
                            Text(
                                text = resource.toString(),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//private fun NumberPicker(
//    pickedNumber: Int,
//    onNumberSelected: (Int) -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .padding(bottom = 8.dp)
//            .clip(RoundedCornerShape(12.dp))
//    ) {
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Blue),
//            factory = { context ->
//                NumberPicker(context).apply {
//                    setOnValueChangedListener { _, _, number -> onNumberSelected(number) }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        textColor = ContextCompat.getColor(context, R.color.white)
//                    }
//                    minValue = 2
//                    maxValue = 12
//                    value = pickedNumber
//                }
//            }
//        )
//    }
//}

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf(2)
}

@Composable
fun Picker(
    items: List<Int>,
    state: PickerState = rememberPickerState(),
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Int.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index).toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle.copy(color = Color.White),
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .height(1.dp),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = (itemHeightDp * visibleItemsMiddle) + itemHeightDp)
                .height(1.dp),
            color = dividerColor
        )
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }
/*** Base Code  Ends here. ***/

/*** Preview Code  Starts here. ***/
@Composable
fun NumberPickerDemo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        val values = remember { (2..12).toList() }
        val valuesPickerState = rememberPickerState()

        Picker(
            state = valuesPickerState,
            items = values,
            visibleItemsCount = 5,
            modifier = Modifier.fillMaxWidth(0.5f),
            textModifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontSize = 32.sp),
            dividerColor = Color(0xFFE8E8E8)
        )

        Text(
            text = "Result: ${valuesPickerState.selectedItem}",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(500),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.5f)
                .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(size = 8.dp))
                .padding(vertical = 10.dp, horizontal = 16.dp)
        )
    }
}
