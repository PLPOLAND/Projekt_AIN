const DEAD = "#FF1100";
const ALIVE = "#00FF52";

var size = 50;



$(document).ready(function () {
    $(window).resize(function(){$("#kwadraty").css("height", $("#kwadraty").css("width"))});
    $(window).resize(function(){ustaw_wymiary_komorek(size,size)})
    $("#kwadraty").css("height", $("#kwadraty").css("width"));
    podziel_na_komorki(size,size);
    for (let i = 0; i < size; i++) {
        for (let j = 0; j < size; j++) {
            t = Math.random();
            if ( t < 0.1) {
                koloruj(i, j, ALIVE);
            } else {
                koloruj(i, j, DEAD);
            }
        }
    }
    
});
function podziel_na_komorki(x,y) {
    $("#kwadraty").children().remove();
    for (let i = 0; i < x; i++) {
        row =$('<div class="row"></div>')
        for (let j = 0; j < y; j++) {
            id = "";
            if (i<10) {
                id = id + "0" + i;
            }
            else
            id = id + i;
            if (j<10) {
                id = id + "0" + j;
            }
            else
            id = id + j;
            tmp = $('<div id="'+id+'" class="komorka"></div>')
            $($(row)).append($(tmp));
        }
        $("#kwadraty").append(row);
    }
    ustaw_wymiary_komorek(x, y);
}

function ustaw_wymiary_komorek(x,y){
    $(".komorka").css("height",( $("#kwadraty").height() /y )- 2);
    $(".komorka").css("width", ($("#kwadraty").width() /x)- 2);
    // console.log($(".komorka"));
    // console.log($("#kwadraty").height() / y);
}

function koloruj(x,y,kolor) {
    id = "";
    if (x < 10) {
        id = id + "0" + x;
    }
    else
        id = id + x;
    if (y < 10) {
        id = id + "0" + y;
    }
    else
        id = id + y;
    // console.log(kolor);
    $("#"+id).css("background-color",kolor)

}