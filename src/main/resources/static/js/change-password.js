async function changePassword(){

    const token =
        localStorage.getItem("token");

    const oldPassword =
        document.getElementById("oldPassword").value;

    const newPassword =
        document.getElementById("newPassword").value;

    const response =
        await fetch(
            "http://localhost:8082/api/auth/change-password",
            {

                method:"POST",

                headers:{

                    "Content-Type":"application/json",
                    "Authorization":"Bearer "+token

                },

                body:JSON.stringify({

                    oldPassword:oldPassword,
                    newPassword:newPassword

                })

            });

    if(response.ok){

        alert("Password Changed");

    }else{

        alert("Change Password Failed");

    }

}