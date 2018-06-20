module.exports = function (context, documents) {

    if (!!documents && documents.length > 0) {
        context.log('Document Id: ', documents[0].id);
        context.bindings.outputDocument = documents[0];
    }
    context.done();
}
