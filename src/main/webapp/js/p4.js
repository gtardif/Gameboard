function P4() {
	var self = this;
	var $canvas = document.getElementById("gameCanvas");
	self.canvas = $canvas.getContext('2d');
	self.width = $canvas.width;
	self.height = $canvas.height;
	self.colWidth = self.width / 7;
	self.lineHeight = self.height / 6;
	self.cellMargin = 5;
	self.cellRadius = (Math.min(self.colWidth, self.lineHeight) - 2 * self.cellMargin) / 2;

	this.drawGrid = function() {
		self.canvas.fillStyle = 'blue';
		self.canvas.fillRect(0, 0, self.width, self.height);
		self.canvas.fillStyle = 'white';

		for ( var raw = 1; raw < 7; raw++) {
			for ( var col = 1; col < 8; col++) {
				self.drawPawn(col, raw, 'white');
			}
		}
	};

	this.drawPawn = function(col, raw, color) {
		self.canvas.fillStyle = color;
		var x = (col - 1) * self.colWidth + self.cellMargin + self.cellRadius;
		var y = self.height - (raw - 1) * self.lineHeight - self.cellMargin - self.cellRadius;
		self.drawCircle(self.canvas, x, y, self.cellRadius);
	};

	this.animatePawn = function(col, finalRow, color) {
		self.canvas.globalCompositeOperation = "destination-over";
		self.canvas.fillStyle = color;
		self.canvas.fillStyle = color;
		var x = (col - 1) * self.colWidth + self.cellMargin + self.cellRadius;
		var y = self.height - (finalRow - 1) * self.lineHeight - 2 * self.cellRadius;
		self.drawCircle(self.canvas, x, y, self.cellRadius);
	};

	this.drawCircle = function(canvas, x, y, radius) {
		canvas.beginPath();
		canvas.arc(x, y, radius, 0, Math.PI * 2, true);
		canvas.closePath();
		canvas.fill();
	};
}

module.exports = P4;