var canva;
$(document).ready(function () {
    
    canva = $("#kwadraty");
    canva.height = canva.width;
    document.getElementById('kwadraty').height = parseInt($("#kwadraty").css("width"));
    canva = $("#kwadraty");
    
    
});

void function drawKwadraty(){
    for (let i = 0; i < 100; i++) {

        for (let j = 0; j < 100; j++) {
            canva.drawRect({
                fillStyle: 'black',
                strokeWidth: 0,
                x: i, y: j,
                fromCenter: false,
                width: 2,
                height: 2
            });
        }
    }
}

