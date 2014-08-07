/**
 * switches menudropdown style between block and none
 * @param dropDownId
 */

function toggleMenu(dropDownId) {
	var menuDropDown=document.getElementById(dropDownId);
	
	if (menuDropDown.style.display !== 'block') {
		menuDropDown.style.display = 'block';
	}
	else {
		menuDropDown.style.display = 'none';
	}
}