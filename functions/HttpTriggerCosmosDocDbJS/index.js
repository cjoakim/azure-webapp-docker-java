module.exports = function (context, req) {
    context.log('HttpTriggerMongoJS function invoked');

    if (req.body) {
        // This HTTP request is a POST
        context.log('req.body: ' + JSON.stringify(req.body));
        context.bindings.outputDocument = req.body;
        context.res = {
            status: 201,
            body: "Document created"
        };
    }
    else {
        context.res = {
            status: 400,
            body: "This Function only responds to HTTP POSTs"
        };
    }
    context.done();
};