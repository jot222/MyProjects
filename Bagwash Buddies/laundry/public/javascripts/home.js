var mainApp = {};

(function () {
  var firebase = app_fireBase;
  var adminList = ['jot222@lehigh.edu', 'seh422@lehigh.edu']

  var uid = null;
  firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
      // User is signed in.
      uid = user.uid;
      sessionStorage.setItem("uid", uid);
      sessionStorage.setItem("name", firebase.auth().currentUser.displayName);
      sessionStorage.setItem("email", firebase.auth().currentUser.email);
      //check if admin
      if (adminList.includes(firebase.auth().currentUser.email)) {
        sessionStorage.setItem("admin", true);
        checkAdmin();
      }
      else {
        sessionStorage.setItem("admin", false);
        checkAdmin();
      }
    } else {
      //redirect to login page
      uid = null;
      window.location.replace("index.html");
    }
  });

  let weather = document.querySelector("#weather");
  let temp = document.querySelector("#temp");

  function getWeather() {
    let url =
      "https://api.weatherbit.io/v2.0/current?postal_code=99732&country=US&units=I&key=61e2913ed3da452f8d1e19d17ee1a0fe";
    fetch(url)
      .then(function (response) {
        return response.json();
      })
      .then(function (json) {
        temp.innerHTML = json.data[0].temp + "° Fahrenheit";
        var icon =
          "https://www.weatherbit.io/static/img/icons/" +
          json.data[0].weather.icon +
          ".png";
        document.getElementById("icon").src = icon;
      });
  }
  mainApp.getWeather = getWeather;

  function logOut() {
    sessionStorage.removeItem("uid"); //Removes the uid from sessionStorage
    sessionStorage.removeItem("name"); //Removes the name from sessionStorage
    sessionStorage.removeItem("admin"); //Removes the admin status from sessionStorage
    sessionStorage.removeItem("email");

    firebase.auth().signOut();
  }
  mainApp.logOut = logOut;

  function setTimer(dorm, machineNum, buttonID) {
    var status = document.getElementById(machineNum).cells[1];
    var timer = document.getElementById(machineNum).cells[2];
    var claimant = document.getElementById(machineNum).cells[4];

    status.innerHTML = "IN USE";
    claimant.innerHTML = sessionStorage.getItem("name");
    var expirationTime = new Date();
    //expirationTime.setHours(expirationTime.getHours() + 1);
    //expirationTime.setMinutes(expirationTime.getMinutes() + 1);
    expirationTime.setSeconds(expirationTime.getSeconds() + 30);  //Timer is set for 30 seconds for demo purposes
    expirationTime = expirationTime.getTime();
    let button = document.querySelector("#" + buttonID);
    button.disabled = true;
    button.style.backgroundColor = "red";

    //Check if the user already has a machine claimed
    fetch("/laundry")
      .then(response => response.json())  // convert to json
      .then(function (json) {
        var alreadyHas = false;
        for (let i = 0; i < json.length; i++) {
          if (json[i].machineClaimant == sessionStorage.getItem("name")) {
            alreadyHas = true;
          }
        }

        if (alreadyHas) {
          alert("You have already claimed a machine");
        }
        else {
          fetch('/laundry/' + machineNum)
            // Handle success
            .then(response => response.json())  // convert to json
            .then(function (json) {

              let updatedMachine = {
                dormID: json[0].dormID,
                inService: json[0].inService,
                isClaimed: true,
                machineTime: expirationTime,
                machineClaimant: sessionStorage.getItem("name"),
                claimantEmail: sessionStorage.getItem("email")
              };

              fetch('/laundry/' + machineNum, {
                method: "PUT",
                body: JSON.stringify(updatedMachine),
                headers: { "Content-type": "application/json; charset=UTF-8" }
              })
                .then(response => response.json())  // convert to json
                .then(json => console.log(json))    //print data
                .catch(function (json) { });
            })
            .catch(err => console.log('Read Failed', err));
        }

      })
      .catch(err => console.log('Read Failed', err));
  }
  mainApp.setTimer = setTimer;



  /**
   * Adds a row to the table. Called on page load only
   */
  function addRow(dormNum, machineID, isClaimed) {
    var dormID = "";
    if (dormNum == 1) {
      dormID = "dorm1machines";
    }
    if (dormNum == 2) {
      dormID = "dorm2machines";
    }
    if (dormNum == 3) {
      dormID = "dorm3machines";
    }
    var table = document.getElementById(dormID);
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    row.id = machineID;
    row.insertCell(0).innerHTML = machineID;
    if (isClaimed) {
      row.insertCell(1).innerHTML = "IN USE";

    }
    else {
      row.insertCell(1).innerHTML = "OPEN";
    }
    row.insertCell(2).innerHTML = "-- --";
    const buttonID = "machine" + dormNum + "-" + machineID;
    const statement = "'" + dormID + "', '" + machineID + "', '" + buttonID + "'";
    var buttonHTML =
      '<button class="btn btn-success" id=' +
      buttonID +
      ' onclick="mainApp.setTimer(' +
      statement +
      ');">Claim</button>';
    row.insertCell(3).innerHTML = buttonHTML;
    row.insertCell(4).innerHTML = "-- --";

    var adminHTML = '<button class = "btn btn-warning adminButton" onclick="mainApp.launchModal(' + machineID + ')">Admin Options</button>'
    row.insertCell(5).innerHTML = adminHTML;

    let button = document.querySelector("#" + buttonID);
    if (isClaimed) {
      button.disabled = true;
      button.style.backgroundColor = "red";
    }

  }
  mainApp.addRow = addRow;

  /**
   * This function is called when the page loads and populates the page with the initial values
   */
  function populateTables(machineArr, dormID) {
    for (let i = 0; i < machineArr.length; i++) {
      var machineID = machineArr[i].machineID;
      var isClaimed = machineArr[i].isClaimed;

      addRow(dormID, machineID, isClaimed,);
    }
  }


  /**
   * Reset machines to default in database
   */
  function resetMachine(machineID, dormID) {

    let updatedMachine = {
      dormID: dormID,
      isClaimed: false,
      machineClaimant: "",
      inService: true,
      machineTime: null,
      claimantEmail: ""
    };
    fetch('/laundry/' + machineID, {
      method: "PUT",
      body: JSON.stringify(updatedMachine),
      headers: { "Content-type": "application/json" }
    })
      .then(response => response.json())  // convert to json
      .then(json => console.log(json))    //print data
      .catch(err => console.log('Update Failed + JSON IS ' + JSON.stringify(updatedMachine), err));
  }

  /**
   * This function populates the web page with the initial values of the machines in the database
   */
  function loadMachines() {

    checkAdmin(); //Called here again so that admin state persists even on page reload

    fetch("/laundry")
      .then(response => response.json())  // convert to json
      .then(function (json) {

        var dorm1Machines = [];
        var dorm2Machines = [];
        var dorm3Machines = [];
        for (let i = 0; i < json.length; i++) {
          if (json[i].dormID == 1) { dorm1Machines.push(json[i]) }
          if (json[i].dormID == 2) { dorm2Machines.push(json[i]) }
          if (json[i].dormID == 3) { dorm3Machines.push(json[i]) }
        }

        /*
        //Sort machines by order of machineID
        dorm1Machines.sort(function (a, b) {
          return a.machineID - b.machineID;
        });
        dorm2Machines.sort(function (a, b) {
          return a.machineID - b.machineID;
        });
        dorm3Machines.sort(function (a, b) {
          return a.machineID - b.machineID;
        });
        */

        populateTables(dorm1Machines, 1);
        populateTables(dorm2Machines, 2);
        populateTables(dorm3Machines, 3);


        //This function is called every 1 second
        var x = window.setInterval(function () {
          getMachineData();
        }, 1000);

      })
      .catch(err => console.log('Read Failed', err));
  }


  /**
   * This function is called every seconds and populates the table with the most recent data in the database
   */
  function getMachineData() {

    //Get most recent data and list of machines
    fetch("/laundry")
      .then(response => response.json())  // convert to json
      .then(function (json) {

        let dorm1Machines = [];
        let dorm2Machines = [];
        let dorm3Machines = [];
        for (let i = 0; i < json.length; i++) {
          if (json[i].dormID == 1) { dorm1Machines.push(json[i]) }
          if (json[i].dormID == 2) { dorm2Machines.push(json[i]) }
          if (json[i].dormID == 3) { dorm3Machines.push(json[i]) }
        }


        //var reminderTime = 50000 //50 seconds
        var reminderTime = 15000; //15 Seconds

        //Write dorm1 data to web page
        for (let i = 0; i < dorm1Machines.length; i++) {
          let machineID = dorm1Machines[i].machineID;
          let isClaimed = dorm1Machines[i].isClaimed;
          let machineClaimant = dorm1Machines[i].machineClaimant;
          let inService = dorm1Machines[i].inService;
          let machineTime = dorm1Machines[i].machineTime;
          let claimantEmail = dorm1Machines[i].claimantEmail;
          let buttonID = "machine" + 1 + "-" + machineID;
          let button = document.querySelector("#" + buttonID);
          let status = document.getElementById(machineID).cells[1];
          let timer = document.getElementById(machineID).cells[2];
          let claimant = document.getElementById(machineID).cells[4];

          if (isClaimed) {
            button.disabled = true;
            button.style.backgroundColor = "red";
            status.innerHTML = "In Use";
            claimant.innerHTML = machineClaimant;

            var expirationTime = new Date(machineTime);
            var timeLeft = expirationTime - new Date().getTime();
            min = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
            sec = Math.floor((timeLeft % (1000 * 60)) / 1000);
            let timeLeftString = min + " : " + sec;

            timer.innerHTML = timeLeftString;

            if (timeLeft < reminderTime && timeLeft > reminderTime - 1000) {
              sendEmail(claimantEmail, machineClaimant, machineID)
            }

            if (timeLeft < 1000) {
              resetMachine(machineID, 1);
            }
          }
          else {
            timer.innerHTML = "-- --";
            status.innerHTML = "OPEN";
            button.disabled = false;
            button.style.backgroundColor = "green";
            claimant.innerHTML = "-- --";
          }

          if (!inService) {
            status.innerHTML = "OUT OF ORDER";
            button.style.backgroundColor = "grey";
            button.disabled = true;
            //Hide disabled machines from non-admins
            if (sessionStorage.getItem("admin") === "false") {
              document.getElementById(machineID).style.display = "none";
            }
          }
        }

        //Write dorm 2 data to web page
        for (let i = 0; i < dorm2Machines.length; i++) {
          let machineID = dorm2Machines[i].machineID;
          let isClaimed = dorm2Machines[i].isClaimed;
          let machineClaimant = dorm2Machines[i].machineClaimant;
          let inService = dorm2Machines[i].inService;
          let machineTime = dorm2Machines[i].machineTime;
          let claimantEmail = dorm2Machines[i].claimantEmail;
          let buttonID = "machine" + 2 + "-" + machineID;
          let button = document.querySelector("#" + buttonID);
          let status = document.getElementById(machineID).cells[1];
          let timer = document.getElementById(machineID).cells[2];
          let claimant = document.getElementById(machineID).cells[4];

          if (isClaimed) {
            button.disabled = true;
            button.style.backgroundColor = "red";
            status.innerHTML = "In Use";
            claimant.innerHTML = machineClaimant;

            var expirationTime = new Date(machineTime);
            var timeLeft = expirationTime - new Date().getTime();
            min = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
            sec = Math.floor((timeLeft % (1000 * 60)) / 1000);
            let timeLeftString = min + " : " + sec;

            console.log(timeLeft);
            if (timeLeft < reminderTime && timeLeft > reminderTime - 1000) {
              sendEmail(claimantEmail, machineClaimant, machineID)
            }

            if (timeLeft < 1000) {
              resetMachine(machineID, 2);
            }
            else {
              timer.innerHTML = timeLeftString;
            }

          }
          else {
            timer.innerHTML = "-- --";
            status.innerHTML = "OPEN";
            button.disabled = false;
            button.style.backgroundColor = "green";
            claimant.innerHTML = "-- --";
          }

          if (!inService) {
            status.innerHTML = "OUT OF ORDER";
            button.style.backgroundColor = "grey";
            button.disabled = true;
            if (sessionStorage.getItem("admin") === "false") {
              document.getElementById(machineID).style.display = "none";
            }
          }
        }


        //Write dorm 3 data to web page
        for (let i = 0; i < dorm3Machines.length; i++) {
          let machineID = dorm3Machines[i].machineID;
          let isClaimed = dorm3Machines[i].isClaimed;
          let machineClaimant = dorm3Machines[i].machineClaimant;
          let inService = dorm3Machines[i].inService;
          let machineTime = dorm3Machines[i].machineTime;
          let claimantEmail = dorm3Machines[i].claimantEmail;
          let buttonID = "machine" + 3 + "-" + machineID;
          let button = document.querySelector("#" + buttonID);
          let status = document.getElementById(machineID).cells[1];
          let timer = document.getElementById(machineID).cells[2];
          let claimant = document.getElementById(machineID).cells[4];

          if (isClaimed) {
            button.disabled = true;
            button.style.backgroundColor = "red";
            status.innerHTML = "In Use";
            claimant.innerHTML = machineClaimant;

            var expirationTime = new Date(machineTime);

            var timeLeft = expirationTime - new Date().getTime();
            min = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
            sec = Math.floor((timeLeft % (1000 * 60)) / 1000);
            timeLeftString = min + " : " + sec;

            timer.innerHTML = timeLeftString;

            if (timeLeft < reminderTime && timeLeft > reminderTime - 1000) {
              sendEmail(claimantEmail, machineClaimant, machineID)
            }

            if (timeLeft < 1000) {
              resetMachine(machineID, 3);
            }
          }
          else {
            timer.innerHTML = "-- --";
            status.innerHTML = "OPEN";
            button.disabled = false;
            button.style.backgroundColor = "green";
            claimant.innerHTML = "-- --";
          }

          if (!inService) {
            //alert("Out of service 3: " + machineID);
            status.innerHTML = "OUT OF ORDER";
            button.style.backgroundColor = "grey";
            button.disabled = true;
            if (sessionStorage.getItem("admin") === "false") {
              document.getElementById(machineID).style.display = "none";
            }
          }
        }
      })
      .catch(err => console.log('Read Failed', err));
  }


  /**
   * Loads the appropriate buttons depending on if the user is an admin or not
   */
  function checkAdmin() {
    if (sessionStorage.getItem("admin") === "true") {
      document.getElementById("adminAdd1").style.display = "block";
      document.getElementById("adminAdd2").style.display = "block";
      document.getElementById("adminAdd3").style.display = "block";
      document.getElementById("adminOptions1").style.display = "block";
      document.getElementById("adminOptions2").style.display = "block";
      document.getElementById("adminOptions3").style.display = "block";


    }
    else {
      document.getElementById("adminAdd1").style.display = "none";
      document.getElementById("adminAdd2").style.display = "none";
      document.getElementById("adminAdd3").style.display = "none";
      document.getElementById("adminOptions1").style.display = "none";
      document.getElementById("adminOptions2").style.display = "none";
      document.getElementById("adminOptions3").style.display = "none";

      let adminButtons = document.getElementsByClassName("adminButton");
      for (let i = 0; i < adminButtons.length; i++) {
        adminButtons[i].style.display = "none";
      }
    }
  }



  function addRowAdmin(dormID) {

    let newMachine = {
      dormID: dormID
    };

    fetch('/laundry', {
      method: "POST",
      body: JSON.stringify(newMachine),
      headers: { "Content-type": "application/json; charset=UTF-8" }
    })
      .then(response => response.json())  // convert to json
      .then(location.reload())    //print data
      .catch(err => console.log('Create Failed: ', err));

  }
  mainApp.addRowAdmin = addRowAdmin;

  function launchModal(machineID) {
    $('#optionsModal').modal('show')
    let delButt = document.getElementById("delButton");
    delButt.setAttribute("onClick", "javascript: mainApp.deleteRowAdmin(" + machineID + ')')
    let upButt = document.getElementById("updateButton");
    upButt.setAttribute("onClick", "javascript: mainApp.updateRowAdmin(" + machineID + ')')

  }
  mainApp.launchModal = launchModal;


  function deleteRowAdmin(machineID) {
    alert("Deleting machine:" + machineID);
    //Fetch delete
    fetch('/laundry/' + machineID, {
      method: "DELETE",
      headers: { "Content-type": "application/json; charset=UTF-8" }
    })
      .then(response => response.json())  // convert to json
      .then(location.reload())    //print data
      .catch(err => console.log('DELETE Failed: ', err));

    location.reload();
  }
  mainApp.deleteRowAdmin = deleteRowAdmin;

  function updateRowAdmin(machineID) {
    let dorm = document.getElementById('selectDorm').value;
    var inService = document.getElementById('selectService').value;

    let updatedMachine = {
      dormID: dorm,
      inService: inService,
      isClaimed: false,
      machineTime: null,
      machineClaimant: ""
    }
    fetch('/laundry/' + machineID, {
      method: "PUT",
      body: JSON.stringify(updatedMachine),
      headers: { "Content-type": "application/json; charset=UTF-8" }
    })
      .then(response => response.json())  // convert to json
      .then(json => console.log(json))    //print data
      .catch(err => console.log('Update Failed ', err));

    location.reload();
  }
  mainApp.updateRowAdmin = updateRowAdmin;


  function sendEmail(target, name, machineID) {

    Email.send({
      SecureToken: "2f661901-7c40-4521-a537-b472120e706b",
      To: target,
      From: "bagwashbuddies@gmail.com",
      Subject: "BAGWASH BUDDIES MACHINE REMINDER",
      Body: "Hey, " + name + ", just a reminder that your claimed machine(Machine: "+machineID+") expires in 5 Minutes!"
    }).then(
      //message => alert("mail has been sent sucessfully")
    );

  }




  loadMachines();


})();
