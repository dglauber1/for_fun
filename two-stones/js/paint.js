function paintMap() {
    var ctx = map.getContext('2d');
    ctx.clearRect(0, 0, map.width, map.height);
	var topLeftX = Math.ceil(CANVAS_X / PIXELS_PER_TILE) - 1; //grid coordinate of top left tile
	var topLeftY = Math.ceil(CANVAS_Y / PIXELS_PER_TILE) - 1; //grid coordinate of top left tile
	for (var i = topLeftX; i < topLeftX + GRID_WIDTH + 1; i++) {
		if (pieces[i] == null) {
			continue;
		}
        for (var j = topLeftY; j < topLeftY + GRID_HEIGHT + 1; j++) {
            if (pieces[i][j] == null) {
                continue;
            } else {
				ctx.beginPath();
				ctx.rect(((-1 * CANVAS_X) % PIXELS_PER_TILE - PIXELS_PER_TILE) % PIXELS_PER_TILE + (i - topLeftX) * PIXELS_PER_TILE, 
						 ((-1 * CANVAS_Y) % PIXELS_PER_TILE - PIXELS_PER_TILE) % PIXELS_PER_TILE + (j - topLeftY) * PIXELS_PER_TILE, PIXELS_PER_TILE, PIXELS_PER_TILE);
				ctx.fillStyle = 'black';
				ctx.fill();
				ctx.closePath();
            }
        }
	}
    if (showGrid) {
        paintGrid();
    }   
}

function paintGrid() {
    var ctx = map.getContext('2d');
    ctx.lineWidth = 2;

    ctx.beginPath();
    ctx.strokeStyle = '#898989';
    for (var i = (-1 * CANVAS_X) % PIXELS_PER_TILE; i < MAP_WIDTH; i+=PIXELS_PER_TILE){
        ctx.moveTo(i, 0);
        ctx.lineTo(i, MAP_HEIGHT);
    }
    for (var i = (-1 * CANVAS_Y) % PIXELS_PER_TILE; i < MAP_HEIGHT; i+=PIXELS_PER_TILE){
        ctx.moveTo(0, i);
        ctx.lineTo(MAP_WIDTH, i);
    }
    ctx.stroke();
    ctx.closePath();
}
