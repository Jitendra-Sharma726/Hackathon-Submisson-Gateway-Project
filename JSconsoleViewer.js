const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

console.log("Please PASTE your current browser URL below:");

rl.question('> ', (inputUrl) => {
    let domain = inputUrl.trim();
    
    domain = domain.replace("https://", "").replace("http://", "");
    if (domain.includes("/")) {
        domain = domain.substring(0, domain.indexOf("/"));
    }
    if (domain.includes("#")) {
        domain = domain.substring(0, domain.indexOf("#"));
    }
    
    const protocolPrefix = "https://";
    const firstDotIndex = domain.indexOf('.');
    let backendUrl;
    if (firstDotIndex === -1) {
        backendUrl = "http://localhost:3000";
    } else {
        const subdomain = domain.substring(0, firstDotIndex);
        const restOfDomain = domain.substring(firstDotIndex);
        backendUrl = protocolPrefix + subdomain + restOfDomain;
    }

    console.log("\nHere is your H2 Console URL:");
    console.log("--------------------------------------------");
    console.log(backendUrl + "/h2-console");
    console.log("--------------------------------------------");

    console.log("\nLogin Details:");
    console.log("    JDBC URL:  jdbc:h2:mem:hackathondb");
    console.log("    User Name: sa");
    console.log("    Password:  (Leave Empty)");
    rl.close();
});
