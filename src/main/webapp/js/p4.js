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

		for ( var line = 0; line < 6; line++) {
			for ( var col = 0; col < 7; col++) {
				var x = col * self.colWidth + self.cellMargin + self.cellRadius;
				var y = line * self.lineHeight + self.cellMargin + self.cellRadius;
				self.drawCircle(self.canvas, x, y, self.cellRadius);
			}
		}
	};

	this.drawCircle = function(canvas, x, y, radius) {
		canvas.beginPath();
		canvas.arc(x, y, radius, 0, Math.PI * 2, true);
		canvas.closePath();
		canvas.fill();
	};
}

module.exports = P4;