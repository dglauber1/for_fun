MAP_WIDTH = window.innerWidth; 
MAP_HEIGHT = window.innerHeight;
PIXELS_PER_TILE = 50;
MAX_PIXELS_PER_TILE = 200;
MIN_PIXELS_PER_TILE = 40;
GRID_WIDTH = MAP_WIDTH / PIXELS_PER_TILE;
GRID_HEIGHT = MAP_HEIGHT / PIXELS_PER_TILE;
CANVAS_X = 25; //pixel displacement from grid origin (1 so top left tile initially is visible)
CANVAS_Y = 25; //pixel displacement from grid origin

map = document.getElementById('map');
$("#zoomSlider")[0].min = MIN_PIXELS_PER_TILE;
$("#zoomSlider")[0].max = MAX_PIXELS_PER_TILE;
$("#zoomSlider")[0].value = PIXELS_PER_TILE;

map.width = MAP_WIDTH;
map.height = MAP_HEIGHT;

pieces = {}; //2D array. elements: (1 -> human piece, -1 -> computer piece)
showGrid = true;
paintMap();

//$("#showGrid").on("click", function() {
//    showGrid = this.checked;
//    paintMap();
//});

computerthinking = false;
mousedown = false;
mapdragged = false;
mouse_x = 0;
mouse_y = 0;

function handleMouseClick() {
	if (computerthinking) {
		return;
	}
	var gridX = Math.floor((mouse_x + CANVAS_X) / PIXELS_PER_TILE); 
	var gridY = Math.floor((mouse_y + CANVAS_Y) / PIXELS_PER_TILE);
	if (pieces[gridX] != null && pieces[gridX][gridY] != null) {
		return;
	}
	if (pieces[gridX] == null) {
		pieces[gridX] = {};
	}
	pieces[gridX][gridY] = 1;
	paintMap();
	console.log(gridX);
	console.log(gridY);
}


function panMap(newPixelsPerTile, mouseX, mouseY) {
	var newGridWidth = MAP_WIDTH / newPixelsPerTile;
	var newGridHeight = MAP_HEIGHT / newPixelsPerTile;

    var mouseTilesXOld = mouseX / PIXELS_PER_TILE;
    var mouseTilesYOld = mouseY / PIXELS_PER_TILE;

    var mouseTilesXNew = mouseX / newPixelsPerTile;
    var mouseTilesYNew = mouseY / newPixelsPerTile;

    var changeToCanvasX = (mouseTilesXOld - mouseTilesXNew) * newPixelsPerTile;
    var changeToCanvasY = (mouseTilesYOld - mouseTilesYNew) * newPixelsPerTile;
    CANVAS_X = CANVAS_X * GRID_WIDTH / newGridWidth + changeToCanvasX;
    CANVAS_Y = CANVAS_Y * GRID_HEIGHT / newGridHeight + changeToCanvasY;
}

function zoomMap(newPixelsPerTile, mouseX, mouseY) {
    var changeFactor = PIXELS_PER_TILE / newPixelsPerTile;
    panMap(newPixelsPerTile, mouseX, mouseY);
    GRID_WIDTH = MAP_WIDTH / newPixelsPerTile;
	GRID_HEIGHT = MAP_HEIGHT / newPixelsPerTile;
    PIXELS_PER_TILE = newPixelsPerTile;
    paintMap();
}

function zoomScroll(event) {
    event.preventDefault();
    var newPixelsPerTile = PIXELS_PER_TILE * (1 + event.originalEvent.wheelDelta / 120 / 50);
    if (newPixelsPerTile < $("#zoomSlider")[0].min) {
        return;
    }
    if (newPixelsPerTile > $("#zoomSlider")[0].max) {
        return;
    }
    
    mouse_x = event.pageX;
    mouse_y = event.pageY;
    mouse_x -= map.offsetLeft;
    mouse_y -= map.offsetTop;
    zoomMap(newPixelsPerTile, mouse_x, mouse_y);
    $("#zoomSlider")[0].value = newPixelsPerTile;
}

$(window).on("resize", function(event){
	MAP_WIDTH = window.innerWidth;
	MAP_HEIGHT = window.innerHeight;
	GRID_WIDTH = MAP_WIDTH / PIXELS_PER_TILE;
	GRID_HEIGHT = MAP_HEIGHT / PIXELS_PER_TILE;
	map.width = MAP_WIDTH;
	map.height = MAP_HEIGHT;
	paintMap();
});

//mousewheel isn't recognized by firefox browsers
$("#map").on('mousewheel', function(event){
    zoomScroll(event);
});

$("#zoomSlider").on("input", function(event) {
    var sliderValue = parseFloat($("#zoomSlider")[0].value);
    zoomMap(sliderValue, MAP_WIDTH / 2, MAP_WIDTH / 2);
});

$("#map").on('mousedown', function(event){
	mousedown = true;
	mouse_x = event.pageX;
	mouse_y = event.pageY;
    initial_canvas_x = CANVAS_X;
    initial_canvas_y = CANVAS_Y;
});

$("#map").on('mouseup', function(event){
    if (!mapdragged) {
        mouse_x = event.pageX;
        mouse_y = event.pageY;
        mouse_x -= map.offsetLeft;
        mouse_y -= map.offsetTop;
		handleMouseClick();
    }
    mousedown = false;
    mapdragged = false;
});

$(window).on('mousemove', function(event){
	if (mousedown) {
        event.preventDefault();
        if (!mapdragged) {
            mapdragged = true;
        }
		CANVAS_X = initial_canvas_x + mouse_x - event.pageX;
		CANVAS_Y = initial_canvas_y + mouse_y - event.pageY;
		paintMap();
	}
});