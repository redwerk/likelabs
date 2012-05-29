#import "ReviewService.h"
#import "Review.h"
#import "URLUtil.h"
#import "SettingsDao.h"

@implementation ReviewService

- (void)postReview:(Review *)review {
    if (review.user.phone && review.user.phone.length) {
        SettingsDao *dao = [[SettingsDao alloc] init];
        ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[URLUtil getReviewUrlForTablet:dao.tabletId]];
        [request addRequestHeader:CONTENT_TYPE_KEY value:CONTENT_TYPE_VALUE];

        [request addRequestHeader:TABLET_API_KEY value:dao.apiKey];
        [dao release];
        
        [request appendPostData:[[review serializeToXml] dataUsingEncoding:NSUTF8StringEncoding]];
        [request setDelegate:self];
        [request startAsynchronous];
    }
}

- (void)requestFinished:(ASIHTTPRequest *)request {
    NSLog(@"Response status: %d", request.responseStatusCode);
    NSLog(@"Response string: %@", request.responseString);
}

- (void)requestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error: %@", request.error);
}

- (void)dealloc {
    [super dealloc];
}

@end
