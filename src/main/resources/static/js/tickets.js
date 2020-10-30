const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');
 
const FabricCAServices = require('fabric-ca-client');
const {FileSystemWallet, X509WalletMixin, Gateway} = require('fabric-network');
 
const ccpPath = path.resolve(__dirname, '..' , 'a', 'connection.json');
const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
const ccp = JSON.parse(ccpJSON);
 
// Create a new CA client for interacting with the CA.
const caURL = ccp.certificateAuthorities['ca.example.com'].url;
const ca = new FabricCAServices(caURL);
 
// Create a new file system based wallet for managing identities.
const walletPath = path.join(process.cwd(), 'wallet');
const wallet = new FileSystemWallet(walletPath);
console.log('Wallet path: ${walletPath}');
 
/* GET */
router.get('/wallet', async(req, res, next) => {
    try{
        // Check to see if we've already enrolled the admin user.
        const adminExists = await wallet.exists('admin');
        if (!adminExists) {
        // Enroll the admin user, and import the new identity into the wallet.
        const enrollment = await ca.enroll({ enrollmentID: 'admin', enrollmentSecret: 'adminpw' });
        const identity = X509WalletMixin.createIdentity('Org1MSP', enrollment.certificate, enrollment.key.toBytes());
        wallet.import('admin', identity);
        console.log('Successfully enrolled admin user "admin" and imported it into the wallet');
 
        }   
 
        // Check to see if we've already enrolled the user.
        const userExists = await wallet.exists('user1');
        if (!userExists) {
            // Create a new gateway for connecting to our peer node.
            const gateway = new Gateway();
            await gateway.connect(ccp, { wallet, identity: 'admin', discovery: { enabled: false } });
 
            // Get the CA client object from the gateway for interacting with the CA.
            const ca = gateway.getClient().getCertificateAuthority();
            const adminIdentity = gateway.getCurrentIdentity();
 
            // Register the user, enroll the user, and import the new identity into the wallet.
            const secret = await ca.register({ affiliation: 'org1.department1', enrollmentID: 'user1', role: 'client' }, adminIdentity);
            const enrollment = await ca.enroll({ enrollmentID: 'user1', enrollmentSecret: secret });
            const userIdentity = X509WalletMixin.createIdentity('Org1MSP', enrollment.certificate, enrollment.key.toBytes());
            wallet.import('user1', userIdentity);
            console.log('Successfully registered and enrolled admin user "user1" and imported it into the wallet');
 
        }
 
        res.json({"msg":"ok"});
    }catch(e){
        console.log(e);
        res.json({"msg":"connect error"});
    }
  });
  
 
/* GET */
router.get('/queryAllTickets', async (req, res, next) => {
    try{
    console.log("queryAllTickets..." );
    const userExists = await wallet.exists('user1');
        if (!userExists) {
            console.log('An identity for the user "user1" does not exist in the wallet');
            await res.json({'msg':'연결부터 해주세요'});
            return;
        }
 
        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'user1', discovery: { enabled: false } });
 
        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');
 
        // Get the contract from the network.
        const contract = network.getContract('ticket');
       
        const result = await contract.evaluateTransaction('queryAllTickets');
 
        console.log('Transaction has been evaluated, result is: ${result.toString()}');
        res.json({'msg':result.toString()});
 
    }catch(e){
        console.log(e);
        res.json({'msg':'query error'});
    }
    }
);
 
/* POST */
router.post('/createTickets', async (req, res, next) => {
    try{
    console.log("createTickets : ", req.body.name);
    const userExists = await wallet.exists('user1');
        if (!userExists) {
            console.log('An identity for the user "user1" does not exist in the wallet');
            await res.json({'msg':'연결부터 해주세요'});
            return;
        }
 
        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, {wallet, identity: 'user1', discovery: {enabled: false}});
 
        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');
 
        // Get the contract from the network.
        const contract = network.getContract('ticket');
 
        await contract.submitTransaction('createTicket','${req.body.name}','${req.body.type}'
        ,'${req.body.grade}','${req.body.date}','${req.body.owner}');

        console.log('Transaction has been submitted');
        res.json({'msg':'ok'});
    }catch(e){
        console.log(e);
        res.json({'msg':'send error'});
    }
    }
);
 
module.exports = router;
