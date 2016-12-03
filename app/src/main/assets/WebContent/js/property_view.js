/*  property_view.js
    Author: Hussam Fetyan
    This file contains the functions required
    to apply changes to the property view document dynamically
*/


var listing_date = new Date(2016, 1, 10, 0, 0, 0, 0);

function updateFields(){
    var current_date = new Date();
    var total_seconds_elapsed = Math.floor((current_date - listing_date) / 1000);

    var days_elapsed = Math.floor(total_seconds_elapsed / (24 * 60 * 60));
    var less_than_day = total_seconds_elapsed % (24 * 60 * 60);
    var hours_elapsed = Math.floor(less_than_day / (60 * 60));
    var less_than_hour = less_than_day % (60 * 60);
    var minutes_elapsed = Math.floor(less_than_hour / 60);
    var seconds_elapsed = less_than_hour % 60;

    document.getElementById("days").innerHTML = days_elapsed;
    document.getElementById("hours").innerHTML = hours_elapsed;
    document.getElementById("mins").innerHTML = minutes_elapsed;
    document.getElementById("sec").innerHTML = seconds_elapsed;
}

setInterval(updateFields, 1000);


function showNextImage()
{
	var thumbs = $("aside img");
	var count_of_thumbs = thumbs.length;	
	var current_large = $("#enlarged")[0];
	
	for (i = 0; i < count_of_thumbs; i++)
	{
		if (thumbs[i].src == current_large.src)
		{
			current_large.src = thumbs[(i + 1) % count_of_thumbs].src;
			break;
		}		
	}	
}

function showPrevImage()
{
	var thumbs = $("aside img");
	var count_of_thumbs = thumbs.length;	
	var current_large = $("#enlarged")[0];
	
	for (i = 0; i < count_of_thumbs; i++){
		if (thumbs[i].src == current_large.src)
		{
			if (i == 0)
			{
				current_large.src = thumbs[count_of_thumbs - 1].src;
			}
			else
			{
				current_large.src = thumbs[i - 1].src;
			}
			break;
		}		
	}	
}

function enlargeImage(evt){
    var img_src = evt.target.src;

    $("#enlarged").attr("src", img_src); 

	$("#container").toggleClass("large hidden");
	
	var left_arr = $("<span>").attr("id", "left_arrow").text("<").css({fontWeight: "bolder", marginRight: "30px", color: "white"}).on("click", showPrevImage);
	var right_arr = $("<span>").attr("id", "right_arrow").text(">").css({fontWeight: "bolder", marginLeft: "30px", color: "white"}).on("click", showNextImage);
	
	$("#close_btn").before(left_arr);
	$("#close_btn").after(right_arr);
}

function shrinkImage(){
    $("#container").toggleClass("large hidden");
	$("#left_arrow, #right_arrow").remove();
}

$("#close_btn").on("click", shrinkImage);

$("#pic_1, #pic_2, #pic_3, #pic_4").on("click", enlargeImage);
