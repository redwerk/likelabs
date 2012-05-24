#import "ReviewService.h"
#import "Review.h"
#import "URLUtil.h"

@implementation ReviewService

static NSUInteger const DEFAULT_TABLET_ID = 1;

- (void)postReview:(Review *)review {
    //TODO: turn off debug in ASIHTTPRequestConfig.h
    ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[URLUtil getReviewUrlForTablet:DEFAULT_TABLET_ID]];
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
