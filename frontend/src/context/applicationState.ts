import { createContext, useContext } from "react"
import { ReportInfo } from "../model";

export interface ApplicationState {
    report?: ReportInfo;    
    selectedSpanId?: string;
}

export type ApplicationContextContent = {
    state: ApplicationState;
    setState:(state: ApplicationState) => void;
}

export const ApplicationContext = createContext<ApplicationContextContent>({
    state: {},
    setState: () => {}
});

export const useApplicationContext = () => useContext(ApplicationContext);