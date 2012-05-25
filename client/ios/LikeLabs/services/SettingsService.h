#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"

@class Tablet;
@class Review;


@interface SettingsService : NSObject <ASIHTTPRequestDelegate, NSXMLParserDelegate>

- (void) getSettings;

@end
