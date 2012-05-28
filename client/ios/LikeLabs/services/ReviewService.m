#import "ReviewService.h"
#import "Review.h"
#import "URLUtil.h"
#import "SettingsDao.h"

@implementation ReviewService

static NSUInteger const DEFAULT_TABLET_ID = 1;

- (void)postReview:(Review *)review {
    //TODO: turn off debug in ASIHTTPRequestConfig.h
    ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[URLUtil getReviewUrlForTablet:DEFAULT_TABLET_ID]];
    [request addRequestHeader:CONTENT_TYPE_KEY value:CONTENT_TYPE_VALUE];

    SettingsDao *dao = [[SettingsDao alloc] init];
    [request addRequestHeader:TABLET_API_KEY value:dao.apiKey];
    [dao release];
    
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

- (void)dealloc {
    [super dealloc];
}

@end
