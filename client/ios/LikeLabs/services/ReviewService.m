#import "ReviewService.h"
#import "Review.h"

@implementation ReviewService

- (void)postReview:(Review *)review {
    //TODO: turn off debug in ASIHTTPRequestConfig.h
    ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[NSURL URLWithString:@"http://10.0.1.55:8080/restapi/1.0/tablets/1/reviews"]];
    [request addRequestHeader:@"Content-Type" value:@"application/xml"];
    [request appendPostData:[[review serializeToXml] dataUsingEncoding:NSUTF8StringEncoding]];
    [request setDelegate:self];
    [request startAsynchronous];
}

- (void)requestFinished:(ASIHTTPRequest *)request {
    NSLog(@"Response status: %d", request.responseStatusCode);
}

- (void)requestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error: %@", request.error);
}

@end
