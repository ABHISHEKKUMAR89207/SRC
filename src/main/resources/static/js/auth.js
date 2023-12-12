function signIn() {
    // Get user input (email and password) from the form
    const email = document.getElementById("emailInput").value;
    const password = document.getElementById("passwordInput").value;

    // Make the API request to your backend
    fetch('/sign-in.html', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password,
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            // Assuming the response contains the JWT token
            const jwtToken = data.token;

            // Redirect to sign-in.html with the JWT token as a query parameter
            window.location.href = `/sign-in.html?token=${jwtToken}`;
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle error (display a message, redirect, etc.)
        });
}
