import * as React from 'react';
import type { Span } from '../../model';    
import SpanDetailComponent from '../SpanDetailComponent';
import emptyImg from './img/empty.gif';
import minusImg from './img/minus.gif';
import plusImg from './img/plus.gif';

interface ILogComponentProps {
    reportId: string;
    span: Span;
}

interface ILogComponentState {
    expanded: boolean;
    open: boolean;
}

const LogComponent: React.FC<ILogComponentProps> = ({
    reportId,
    span
}) => {
    const { 
        serviceName, 
        childSpans, 
        label,
        traceId,
        spanId,
        id 
    } = span;
    const hasChildSpan = childSpans?.length ? true : false;
    const [state, setState] = React.useState<ILogComponentState>({expanded: false, open: false});
    const { expanded, open } = state;
    const expandCallback = React.useCallback(() => {
        console.log('expand');
        setState({ ...state, expanded: !expanded });
    }, [ state, expanded ]);
    const openCallback = React.useCallback(() => {
        console.log('open');
        setState({ ...state, open: !open });
    }, [ state, open ]);
    const expandIcon = hasChildSpan ? ( expanded ? minusImg : plusImg) : emptyImg;
    return (
        <div style={{ marginLeft: '20px'}}>
            <h3 onClick={openCallback} className="toggle SUCCESS">
                <div onClick={(e) => { e.stopPropagation(); expandCallback(); }} style={{ float: 'left',  paddingRight: '10px'}}>
                    <img src={expandIcon} />
                </div>
                <span>
                    startDate
                </span>
                &nbsp;-&nbsp;
                <span>
                    finishDate
                </span>
                &nbsp;
                (
                    <span>
                        duration
                    </span>
                )
                &nbsp;
                <span>
                    {serviceName}
                </span>
                :&nbsp;
                {/* <span>
                    {traceId}
                </span>
                :
                <span>
                    {spanId}
                </span>
                :&nbsp; */}
                <span>
                    log.method
                </span>
                <span>
                    {label}
                </span>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>
                    childrenInfo
                </span>
            </h3>
            {open && 
                <SpanDetailComponent reportId={reportId} spanId={id} />
            }
            {expanded &&
                <span>
                    {childSpans?.map((childSpan) => <LogComponent reportId={reportId} span={childSpan} />)}
                </span>
            }
        </div>
    );
  };
  
  export default LogComponent;