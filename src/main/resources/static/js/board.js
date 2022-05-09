
export { Board, settingBoard }

import { stone } from "./stone.js";

export function b() {
	
}

class Board {
	static init(){
		let lineHtml = "";
		for(let i=0;i<19;i++) {
		    let td = "";
		    for(let j=0;j<19;j++) {
		        if((i + 3) % 6 == 0 && (j+3) % 6 == 0) {
		            td += `<td ><div class='point'><div></div></div></td>`;
		        } else {
		            td += `<td></td>`;
		        }
		    }
		
		    lineHtml += `<tr>${td}</tr>`
		}
		$("main table.line").html(lineHtml);
		
		
		let boardHtml = "";
		for(let i=0;i<20;i++) {
		    let td = "";
		    for(let j=0;j<20;j++) {
		        td += `<td></td>`;
		    }
		
		    boardHtml += `<tr>${td}</tr>`
		}
		$("main table.board").html(boardHtml);
	}
	
	
	static setting(board){
		const s = new Stone();
		
		for(let i=0;i<board.length;i++) {
			for(let j=0;j<board[i].length;j++) {
				if(board[i][j] == 0) {
					continue;
				}
				
				const target = $(".board tr").eq(i).find("td").eq(j);
				
				if(board[i][j] == 1) {
					target.html(s.black);
				} else {
					target.html(s.white);
				}
			}
		}				
	}
}



function settingBoard(){
	let lineHtml = "";
	for(let i=0;i<19;i++) {
	    let td = "";
	    for(let j=0;j<19;j++) {
	        if((i + 3) % 6 == 0 && (j+3) % 6 == 0) {
	            td += `<td ><div class='point'><div></div></div></td>`;
	        } else {
	            td += `<td></td>`;
	        }
	    }
	
	    lineHtml += `<tr>${td}</tr>`
	}
	$("main table.line").html(lineHtml);
	
	
	
	let boardHtml = "";
	for(let i=0;i<20;i++) {
	    let td = "";
	    for(let j=0;j<20;j++) {
	        td += `<td></td>`;
	    }
	
	    boardHtml += `<tr>${td}</tr>`
	}
	$("main table.board").html(boardHtml);
}

