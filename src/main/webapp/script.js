//if (window.sessionStorage.myJWT != null)
//	window.sessionStorage.removeItem("myJWT")



function initPage(){
    if (window.sessionStorage.myJWT != null && window.sessionStorage.myJWT != ""){
        console.log(window.sessionStorage.myJWT);

        loadCars();
        
        document.getElementById("loginArea").style.display = "none";
        document.getElementById("navBox").style.display = "block";
        document.getElementById("autoTableBar").style.display = "block";
        document.getElementById("carsTableArea").style.display = "block";
    }
    else{
    	console.log(window.sessionStorage.myJWT);
        document.getElementById("navBox").style.display = "none";
        document.getElementById("autoTableBar").style.display = "none";
        document.getElementById("carsTableArea").style.display = "none";
        document.getElementById("loginArea").style.display = "block";
        console.log("Token not found. Login needed.");
    }
}

function login(event) {
    let formData = new FormData(document.querySelector("#loginform"));
    let encData = new URLSearchParams(formData.entries());


    fetch("restservices/authentication", { method: 'POST', body: encData })
        .then(function(response) {
        	if (response.ok) return response.json();
            else throw "Wrong username/password";
        })
        .then(function(myJson){
        	window.sessionStorage.setItem("myJWT", myJson.JWT);
        	initPage();
        })
        .catch(error => console.log(error));
}

function loadCars(){
    fetch("restservices/cars")
    .then(response =>Promise.all([response.status, response.json()])
    .then(function([status, myJson]) {
        if (status == 200){
           
        	let headersElement = document.getElementById("tableHeaderRow");
        	
            let tableElement = document.getElementById("carsTable").getElementsByTagName("tbody")[0];
            
            tableElement.innerHTML = "";
            
            tableElement.appendChild(headersElement);

            for (let i=0; i < myJson.cars.length; i++){

            	
            	
                let row = document.createElement("tr");

                
                
                
                let carId = myJson.cars[i].carId;
                let model = myJson.cars[i].model;
                let mileage = myJson.cars[i].mileage;
                let reservationsPending = myJson.cars[i].reservationsPending;
                let paidPending = myJson.cars[i].paidPending;

                row.setAttribute("id", "row" + carId);

                let modelCell = document.createElement("td");
                let modelText = document.createTextNode(model);
                modelCell.appendChild(modelText);
                row.appendChild(modelCell);

                let mileageCell = document.createElement("td");
                let mileageText = document.createTextNode(mileage);
                mileageCell.appendChild(mileageText);
                row.appendChild(mileageCell);

                let reservationsPendingCell = document.createElement("td");
                let reservationsPendingText = document.createTextNode(reservationsPending);
                reservationsPendingCell.appendChild(reservationsPendingText);
                row.appendChild(reservationsPendingCell);

                let paidPendingCell = document.createElement("td");
                let paidPendingText = document.createTextNode(paidPending);
                paidPendingCell.appendChild(paidPendingText);
                row.appendChild(paidPendingCell);

                let deleteCell = document.createElement("td");
                let deleteButton = document.createElement("Button");
                deleteButton.addEventListener("click", function(){removeCar(carId, model)}); 
                let deleteText = document.createTextNode("verwijder");

                deleteButton.appendChild(deleteText);
                deleteCell.appendChild(deleteButton);
                row.appendChild(deleteButton);

                tableElement.appendChild(row);


            }

        }
        else if (status == 401 || status == 403)
            alert("You are not authorised to use this functionality");
        else{
            console.log(myJson.error);
        }
    }
    ));
}

function removeCar(id, model){
    if (confirm("Weet je zeker dat je de auto '" + model + "' wilt verwijderen?")){
    	fetch("restservices/cars/" + id, { 
            method: 'DELETE',
            headers:  {'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT")}
        })
        .then(function (response) {
          if (response.ok){
            console.log("Car deleted!");
            loadCars();
          } // response-status = 200 OK
          else if (response.status == 404)
            console.log("Car not found!");
          else console.log("Cannot delete car!");
        })
        .catch(error => console.log(error));
    }
}

function addCar(){
    window.location.href = "./add_car.html";
}

document.querySelector("#addCarButton").addEventListener("click", addCar);
document.querySelector("#loginButton").addEventListener("click", login);


initPage();